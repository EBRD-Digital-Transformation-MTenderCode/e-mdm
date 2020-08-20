package com.procurement.mdm.service.preparation

import com.procurement.mdm.exception.ErrorType
import com.procurement.mdm.exception.InErrorException
import com.procurement.mdm.model.dto.CommandMessage
import com.procurement.mdm.model.dto.ResponseDto
import com.procurement.mdm.model.dto.data.Address
import com.procurement.mdm.model.dto.data.AddressDetails
import com.procurement.mdm.model.dto.data.ClassificationScheme
import com.procurement.mdm.model.dto.data.ContactPoint
import com.procurement.mdm.model.dto.data.CountryDetails
import com.procurement.mdm.model.dto.data.EIData
import com.procurement.mdm.model.dto.data.FS
import com.procurement.mdm.model.dto.data.Identifier
import com.procurement.mdm.model.dto.data.LocalityDetails
import com.procurement.mdm.model.dto.data.OrganizationReference
import com.procurement.mdm.model.dto.data.RegionDetails
import com.procurement.mdm.model.dto.data.ei.EIRequest
import com.procurement.mdm.model.dto.data.ei.EIResponse
import com.procurement.mdm.model.dto.getResponseDto
import com.procurement.mdm.model.entity.Country
import com.procurement.mdm.model.entity.Cpv
import com.procurement.mdm.model.entity.CpvKey
import com.procurement.mdm.model.entity.Cpvs
import com.procurement.mdm.model.entity.CpvsKey
import com.procurement.mdm.model.entity.Language
import com.procurement.mdm.model.entity.UnitKey
import com.procurement.mdm.model.entity.Units
import com.procurement.mdm.repository.CpvRepository
import com.procurement.mdm.repository.CpvsRepository
import com.procurement.mdm.repository.CurrencyRepository
import com.procurement.mdm.repository.LocalityRepository
import com.procurement.mdm.repository.RegionRepository
import com.procurement.mdm.repository.UnitRepository
import com.procurement.mdm.service.ValidationService
import com.procurement.mdm.utils.toObject
import org.springframework.stereotype.Service

interface BudgetDataService {

    fun processEiData(cm: CommandMessage): ResponseDto

    fun processFsData(cm: CommandMessage): ResponseDto
}

@Service
class BudgetDataServiceImpl(
    private val validationService: ValidationService,
    private val organizationDataService: OrganizationDataService,
    private val cpvRepository: CpvRepository,
    private val cpvsRepository: CpvsRepository,
    private val unitRepository: UnitRepository,
    private val regionRepository: RegionRepository,
    private val localityRepository: LocalityRepository,
    private val currencyRepository: CurrencyRepository
) : BudgetDataService {

    override fun processEiData(cm: CommandMessage): ResponseDto {
        val lang = cm.context.language
        val country = validationService.getCountry(languageCode = lang, countryCode = cm.context.country)
        val request = getEiRequest(cm)
        val data = request.convert()
        data.buyer ?: throw InErrorException(ErrorType.INVALID_BUYER)

        organizationDataService.processOrganization(data.buyer, country)

        val updatedItems = getUpdatedItems(data, cm.context.language, cm.context.country)
        val updatedTender = getUpdatedTender(data, cm.context.language, updatedItems)
        val updatedData = data.copy(tender = updatedTender)

        val response = updatedData.convert()

        return getResponseDto(data = response, id = cm.id)
    }

    private fun getUpdatedTender(
        data: EIData,
        languageCode: String,
        updatedItems: List<EIData.Tender.Item>
    ): EIData.Tender {
        val cpvCode = data.tender.classification.id
        val cpvEntity = cpvRepository.findByCpvKeyCodeAndCpvKeyLanguageCode(cpvCode, languageCode = languageCode)
            ?: throw InErrorException(ErrorType.CPV_CODE_UNKNOWN)

        return data.tender.copy(
            items = updatedItems,
            classification = data.tender.classification.copy(
                description = cpvEntity.name,
                scheme = ClassificationScheme.CPV.value()
            ),
            mainProcurementCategory = cpvEntity.mainProcurementCategory
        )
    }

    private fun getUpdatedItems(data: EIData, languageCode: String, countryCode: String): List<EIData.Tender.Item> {
        val language = validationService.getLanguage(languageCode = languageCode, internal = true)
        val country = validationService.getCountry(languageCode = language.code, countryCode = countryCode)

        return data.tender.items.map { item ->
            item.copy(
                //BR-12.1.1
                additionalClassifications = getUpdatedAdditionalClassifications(item, data, language),
                //BR-12.1.2
                classification = getUpdatedClassification(item, data, language),
                //BR-12.1.3
                unit = getUpdatedUnit(item, data, language),
                //BR-12.5.1, BR-12.5.2, BR-12.5.3
                deliveryAddress = getUpdatedDeliveryAddress(item, country)
            )
        }
    }

    private fun checkAndGetCpvsEntities(data: EIData, language: Language): List<Cpvs> {
        val cpvsKeys = data.tender.items
            .asSequence()
            .flatMap { it.additionalClassifications.asSequence() }
            .map { CpvsKey(it.id, language) }
            .toSet()

        val cpvsEntities = cpvsRepository.findAllById(cpvsKeys)
        if (cpvsEntities.size != cpvsKeys.size) throw InErrorException(ErrorType.INVALID_CPVS)
        return cpvsEntities
    }

    private fun checkAndGetCpvEntities(data: EIData, language: Language): List<Cpv> {
        val cpvKeys = data.tender.items
            .map { CpvKey(it.classification.id, language) }
            .toSet()

        val cpvEntities = cpvRepository.findAllById(cpvKeys)
        if (cpvEntities.size != cpvKeys.size) throw InErrorException(ErrorType.INVALID_CPV)
        return cpvEntities
    }

    private fun checkAndGetUnitEntities(data: EIData, language: Language): List<Units> {
        val unitKeys = data.tender.items
            .map { UnitKey(it.unit.id, language) }
            .toSet()

        val unitEntities = unitRepository.findAllById(unitKeys)
        if (unitEntities.size != unitKeys.size) throw InErrorException(ErrorType.INVALID_UNIT)

        return unitEntities
    }

    private fun getUpdatedAdditionalClassifications(
        item: EIData.Tender.Item,
        data: EIData,
        language: Language
    ): List<EIData.Tender.Item.AdditionalClassification> {
        val cpvsEntities = checkAndGetCpvsEntities(data, language)
        val cpvsEntitiesByCode = cpvsEntities.associateBy { it.cpvsKey?.code }

        val updatedAdditionalClassifications = item.additionalClassifications.map { additionalClassification ->
            val correspondingEntity = cpvsEntitiesByCode.getValue(additionalClassification.id)
            additionalClassification.copy(
                scheme = ClassificationScheme.CPVS.value(),
                description = correspondingEntity.name
            )
        }
        return updatedAdditionalClassifications
    }

    private fun getUpdatedClassification(
        item: EIData.Tender.Item,
        data: EIData,
        language: Language
    ): EIData.Tender.Item.Classification {
        val cpvEntities = checkAndGetCpvEntities(data, language)
        val cpvEntitiesByCode = cpvEntities.associateBy { it.cpvKey?.code }

        val updatedClassification = item.classification.let { classification ->
            val correspondingEntity = cpvEntitiesByCode.getValue(classification.id)
            classification.copy(
                scheme = ClassificationScheme.CPV.value(),
                description = correspondingEntity.name
            )
        }
        return updatedClassification
    }

    private fun getUpdatedUnit(
        item: EIData.Tender.Item,
        data: EIData,
        language: Language
    ): EIData.Tender.Item.Unit {
        val unitEntities = checkAndGetUnitEntities(data, language)
        val unitEntitiesByCode = unitEntities.associateBy { it.unitKey?.code }

        val updatedUnit = item.unit.let { unit ->
            val correspondingEntity = unitEntitiesByCode.getValue(unit.id)
            unit.copy(name = correspondingEntity.name)
        }
        return updatedUnit
    }

    private fun getUpdatedDeliveryAddress(
        item: EIData.Tender.Item,
        country: Country
    ): EIData.Tender.Item.DeliveryAddress {
        return item.deliveryAddress.copy(
            addressDetails = item.deliveryAddress.addressDetails.let { addressDetails ->
                addressDetails.copy(
                    //BR-12.5.1
                    country = checkAndGetUpdatedCountry(addressDetails, country),
                    //BR-12.5.2
                    region = checkAndGetUpdatedRegion(addressDetails, country),
                    //BR-12.5.3
                    locality = checkAndGetUpdatedLocality(addressDetails, country)
                )
            }
        )
    }

    private fun checkAndGetUpdatedLocality(
        addressDetails: EIData.Tender.Item.DeliveryAddress.AddressDetails,
        country: Country
    ): EIData.Tender.Item.DeliveryAddress.AddressDetails.Locality? {
        val locality = addressDetails.locality
        val updatedLocality = if (locality != null) {
            val schemeEntity = localityRepository.findOneByScheme(locality.scheme)
            if (schemeEntity != null) {
                val regionEntity = regionRepository
                    .findByRegionKeyCodeAndRegionKeyCountry(addressDetails.region.id, country)
                    ?: throw InErrorException(ErrorType.REGION_UNKNOWN)

                val localityEntity = localityRepository
                    .findByLocalityKeyCodeAndLocalityKeyRegionAndScheme(locality.id, regionEntity, locality.scheme)
                    ?: throw InErrorException(ErrorType.LOCALITY_UNKNOWN)

                locality.copy(description = localityEntity.name)
            } else null
        } else null

        return updatedLocality
    }

    private fun checkAndGetUpdatedRegion(
        addressDetails: EIData.Tender.Item.DeliveryAddress.AddressDetails,
        country: Country
    ): EIData.Tender.Item.DeliveryAddress.AddressDetails.Region {
        val regionEntity = regionRepository.findByRegionKeyCodeAndRegionKeyCountry(addressDetails.region.id, country)
            ?: throw InErrorException(ErrorType.REGION_UNKNOWN)

        return addressDetails.region.copy(
            scheme = regionEntity.scheme,
            description = regionEntity.name
        )
    }

    private fun checkAndGetUpdatedCountry(
        addressDetails: EIData.Tender.Item.DeliveryAddress.AddressDetails,
        country: Country
    ): EIData.Tender.Item.DeliveryAddress.AddressDetails.Country {
        if (addressDetails.country.id != country.countryKey?.code) throw InErrorException(ErrorType.INVALID_COUNTRY)

        return addressDetails.country.copy(
            scheme = country.scheme,
            description = country.name
        )
    }

    override fun processFsData(cm: CommandMessage): ResponseDto {
        val country = validationService.getCountry(languageCode = cm.context.language, countryCode = cm.context.country)
        val entities = currencyRepository.findByCurrencyKeyLanguageCodeAndCountries(cm.context.language, country)
        val dto = getFsData(cm)
        val currencyCode = dto.planning.budget.amount.currency
        entities.asSequence().firstOrNull { it.currencyKey?.code.equals(currencyCode) }
            ?: throw InErrorException(ErrorType.CURRENCY_UNKNOWN)

        val buyer = dto.buyer
        if (buyer != null) {
            organizationDataService.processOrganization(buyer, country)
        }

        val procuringEntity = dto.tender.procuringEntity
        if (procuringEntity != null) {
            organizationDataService.processOrganization(procuringEntity, country)
        }

        return getResponseDto(data = dto, id = cm.id)
    }

    private fun getEiRequest(cm: CommandMessage): EIRequest {
        if (cm.data.size() == 0) throw InErrorException(ErrorType.INVALID_DATA, null, cm.id)
        return toObject(EIRequest::class.java, cm.data)
    }

    private fun getFsData(cm: CommandMessage): FS {
        if (cm.data.size() == 0) throw InErrorException(ErrorType.INVALID_DATA, null, cm.id)
        return toObject(FS::class.java, cm.data)
    }

    private fun EIRequest.convert() = EIData(
        tender = tender.let { tender ->
            EIData.Tender(
                classification = tender.classification.let { classification ->
                    EIData.Tender.Classification(
                        id = classification.id,
                        description = null,
                        scheme = null
                    )
                },
                mainProcurementCategory = null,
                items = tender.items?.map { item ->
                    EIData.Tender.Item(
                        id = item.id,
                        description = item.description,
                        classification = item.classification.let { classification ->
                            EIData.Tender.Item.Classification(
                                id = classification.id,
                                scheme = null,
                                description = null
                            )
                        },
                        additionalClassifications = item.additionalClassifications
                            ?.map { additionalClassification ->
                                EIData.Tender.Item.AdditionalClassification(
                                    id = additionalClassification.id,
                                    description = null,
                                    scheme = null
                                )
                            }.orEmpty(),
                        deliveryAddress = item.deliveryAddress.let { address ->
                            EIData.Tender.Item.DeliveryAddress(
                                streetAddress = address.streetAddress,
                                postalCode = address.postalCode,
                                addressDetails = address.addressDetails.let { addressDetails ->
                                    EIData.Tender.Item.DeliveryAddress.AddressDetails(
                                        country = addressDetails.country.let { country ->
                                            EIData.Tender.Item.DeliveryAddress.AddressDetails.Country(
                                                id = country.id,
                                                description = country.description,
                                                scheme = country.scheme
                                            )
                                        },
                                        region = addressDetails.region.let { region ->
                                            EIData.Tender.Item.DeliveryAddress.AddressDetails.Region(
                                                id = region.id,
                                                description = region.description,
                                                scheme = region.scheme
                                            )
                                        },
                                        locality = addressDetails.locality?.let { locality ->
                                            EIData.Tender.Item.DeliveryAddress.AddressDetails.Locality(
                                                id = locality.id,
                                                description = locality.description,
                                                scheme = locality.scheme
                                            )
                                        }
                                    )
                                }
                            )
                        },
                        quantity = item.quantity,
                        unit = item.unit.let { unit ->
                            EIData.Tender.Item.Unit(
                                id = unit.id,
                                name = null
                            )
                        }
                    )
                }.orEmpty()
            )
        },
        buyer = buyer.let { buyer ->
            OrganizationReference(
                id = null,
                details = null,
                additionalIdentifiers = null,
                buyerProfile = null,
                persones = null,
                name = buyer.name,
                address = buyer.address.let { address ->
                    Address(
                        streetAddress = address.streetAddress,
                        postalCode = null,
                        addressDetails = address.addressDetails.let { addressDetails ->
                            AddressDetails(
                                country = addressDetails.country.let { country ->
                                    CountryDetails(
                                        id = country.id,
                                        scheme = null,
                                        uri = null,
                                        description = null
                                    )
                                },
                                region = addressDetails.region.let { region ->
                                    RegionDetails(
                                        id = region.id,
                                        scheme = null,
                                        uri = null,
                                        description = null
                                    )
                                },
                                locality = addressDetails.locality.let { locality ->
                                    LocalityDetails(
                                        id = locality.id,
                                        description = locality.description,
                                        scheme = locality.scheme,
                                        uri = null
                                    )
                                }
                            )
                        }
                    )
                },
                contactPoint = buyer.contactPoint.let { contactPoint ->
                    ContactPoint(
                        name = contactPoint.name,
                        email = contactPoint.email,
                        faxNumber = contactPoint.faxNumber,
                        telephone = contactPoint.telephone,
                        url = contactPoint.url
                    )
                },
                identifier = buyer.identifier.let { identifier ->
                    Identifier(
                        id = identifier.id,
                        uri = identifier.uri,
                        scheme = identifier.scheme,
                        legalName = identifier.legalName
                    )
                }
            )
        }
    )

    private fun EIData.convert() = EIResponse(
        tender = tender.let { tender ->
            EIResponse.Tender(
                classification = tender.classification
                    .let { classification ->
                        EIResponse.Tender.Classification(
                            id = classification.id,
                            description = classification.description,
                            scheme = classification.scheme
                        )
                    },
                mainProcurementCategory = tender.mainProcurementCategory,
                items = tender.items
                    .map { item ->
                        EIResponse.Tender.Item(
                            id = item.id,
                            description = item.description,
                            classification = item.classification
                                .let { classification ->
                                    EIResponse.Tender.Item.Classification(
                                        id = classification.id,
                                        description = classification.description,
                                        scheme = classification.scheme
                                    )
                                },
                            additionalClassifications = item.additionalClassifications
                                .map { additionalClassification ->
                                    EIResponse.Tender.Item.AdditionalClassification(
                                        id = additionalClassification.id,
                                        scheme = additionalClassification.scheme,
                                        description = additionalClassification.description
                                    )
                                },
                            deliveryAddress = item.deliveryAddress
                                .let { address ->
                                    EIResponse.Tender.Item.DeliveryAddress(
                                        streetAddress = address.streetAddress,
                                        postalCode = address.postalCode,
                                        addressDetails = address.addressDetails.let { addressDetails ->
                                            EIResponse.Tender.Item.DeliveryAddress.AddressDetails(
                                                country = addressDetails.country.let { country ->
                                                    EIResponse.Tender.Item.DeliveryAddress.AddressDetails.Country(
                                                        id = country.id,
                                                        description = country.description,
                                                        scheme = country.scheme
                                                    )
                                                },
                                                region = addressDetails.region.let { region ->
                                                    EIResponse.Tender.Item.DeliveryAddress.AddressDetails.Region(
                                                        id = region.id,
                                                        description = region.description,
                                                        scheme = region.scheme
                                                    )
                                                },
                                                locality = addressDetails.locality?.let { locality ->
                                                    EIResponse.Tender.Item.DeliveryAddress.AddressDetails.Locality(
                                                        id = locality.id,
                                                        description = locality.description,
                                                        scheme = locality.scheme
                                                    )
                                                }
                                            )
                                        }
                                    )
                                },
                            quantity = item.quantity,
                            unit = item.unit
                                .let { unit ->
                                    EIResponse.Tender.Item.Unit(
                                        id = unit.id,
                                        name = unit.name
                                    )
                                }
                        )
                    }
            )
        },
        buyer = buyer?.let { buyer ->
            EIResponse.Buyer(
                name = buyer.name,
                address = buyer.address
                    ?.let { address ->
                        EIResponse.Buyer.Address(
                            streetAddress = address.streetAddress,
                            addressDetails = address.addressDetails
                                .let { addressDetails ->
                                    EIResponse.Buyer.Address.AddressDetails(
                                        country = addressDetails.country
                                            .let { country ->
                                                EIResponse.Buyer.Address.AddressDetails.Country(
                                                    id = country.id,
                                                    description = country.description,
                                                    scheme = country.scheme,
                                                    uri = country.uri
                                                )
                                            },
                                        region = addressDetails.region
                                            .let { region ->
                                                EIResponse.Buyer.Address.AddressDetails.Region(
                                                    id = region.id,
                                                    description = region.description,
                                                    scheme = region.scheme,
                                                    uri = region.uri
                                                )
                                            },
                                        locality = addressDetails.locality
                                            .let { locality ->
                                                EIResponse.Buyer.Address.AddressDetails.Locality(
                                                    id = locality.id,
                                                    description = locality.description,
                                                    scheme = locality.scheme,
                                                    uri = locality.uri
                                                )
                                            }
                                    )
                                }
                        )
                    },
                contactPoint = buyer.contactPoint
                    ?.let { contactPoint ->
                        EIResponse.Buyer.ContactPoint(
                            name = contactPoint.name,
                            email = contactPoint.email,
                            faxNumber = contactPoint.faxNumber,
                            telephone = contactPoint.telephone,
                            url = contactPoint.url
                        )
                    },
                identifier = buyer.identifier
                    ?.let { identifier ->
                        EIResponse.Buyer.Identifier(
                            id = identifier.id,
                            uri = identifier.uri,
                            scheme = identifier.scheme,
                            legalName = identifier.legalName
                        )
                    }
            )
        }
    )
}

