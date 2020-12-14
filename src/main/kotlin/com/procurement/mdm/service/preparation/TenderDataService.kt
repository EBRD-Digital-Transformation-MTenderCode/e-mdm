package com.procurement.mdm.service.preparation

import com.procurement.mdm.exception.ErrorType
import com.procurement.mdm.exception.InErrorException
import com.procurement.mdm.model.dto.CommandMessage
import com.procurement.mdm.model.dto.ResponseDto
import com.procurement.mdm.model.dto.data.ClassificationScheme
import com.procurement.mdm.model.dto.data.ClassificationTD
import com.procurement.mdm.model.dto.data.CountryDetails
import com.procurement.mdm.model.dto.data.ItemTD
import com.procurement.mdm.model.dto.data.ItemUnitTD
import com.procurement.mdm.model.dto.data.LocalityDetails
import com.procurement.mdm.model.dto.data.RegionDetails
import com.procurement.mdm.model.dto.data.TD
import com.procurement.mdm.model.dto.data.ap.AggregationPlan
import com.procurement.mdm.model.dto.data.ap.UpdateAggregationPlan
import com.procurement.mdm.model.dto.getResponseDto
import com.procurement.mdm.model.entity.Country
import com.procurement.mdm.model.entity.Cpv
import com.procurement.mdm.model.entity.CpvKey
import com.procurement.mdm.model.entity.Cpvs
import com.procurement.mdm.model.entity.CpvsKey
import com.procurement.mdm.model.entity.Language
import com.procurement.mdm.model.entity.Region
import com.procurement.mdm.model.entity.UnitKey
import com.procurement.mdm.model.entity.Units
import com.procurement.mdm.repository.CpvRepository
import com.procurement.mdm.repository.CpvsRepository
import com.procurement.mdm.repository.LocalityRepository
import com.procurement.mdm.repository.PmdRepository
import com.procurement.mdm.repository.RegionRepository
import com.procurement.mdm.repository.TranslateRepository
import com.procurement.mdm.repository.UnitRepository
import com.procurement.mdm.service.ValidationService
import com.procurement.mdm.utils.toObject
import org.springframework.stereotype.Service

interface TenderDataService {

    fun processTenderData(cm: CommandMessage): ResponseDto
    fun validateAP(cm: CommandMessage): ResponseDto
    fun enrichDataForUpdateAP(cm: CommandMessage): ResponseDto
}

@Service
class TenderDataServiceServiceImpl(private val validationService: ValidationService,
                                   private val cpvRepository: CpvRepository,
                                   private val cpvsRepository: CpvsRepository,
                                   private val unitRepository: UnitRepository,
                                   private val regionRepository: RegionRepository,
                                   private val localityRepository: LocalityRepository,
                                   private val translateRepository: TranslateRepository,
                                   private val organizationDataService: OrganizationDataService,
                                   private val addressDataService: AddressDataService,
                                   private val pmdRepository: PmdRepository) : TenderDataService {

    override fun processTenderData(cm: CommandMessage): ResponseDto {
        val lang = cm.context.language
        val pmd = cm.context.pmd ?: throw InErrorException(ErrorType.INVALID_PMD)
        val language = validationService.getLanguage(languageCode = lang, internal = true)
        val country = validationService.getCountry(languageCode = lang, countryCode = cm.context.country)
        val dto = getData(cm)
        processItems(dto, language)
        dto.tender.procuringEntity?.let {
            organizationDataService.processOrganization(dto.tender.procuringEntity, country)
        }
        if (dto.tender.lots != null) {
            dto.tender.lots.asSequence()
                .filter { it.placeOfPerformance != null }
                .forEach { addressDataService.processAddress(it.placeOfPerformance!!.address, country) }
        }
        processTranslate(dto, country, lang, pmd)
        return getResponseDto(data = dto, id = cm.id)
    }

    override fun validateAP(cm: CommandMessage): ResponseDto {
        val lang = cm.context.language
        val pmd = cm.context.pmd ?: throw InErrorException(ErrorType.INVALID_PMD)
        val language = validationService.getLanguage(languageCode = lang, internal = true)
        val country = validationService.getCountry(languageCode = lang, countryCode = cm.context.country)
        val request = toObject(AggregationPlan::class.java, cm.data)

        val submissionMethodRationale = listOf(getTranslate("submissionMethodRationale", lang))
        val submissionMethodDetails = getTranslate("submissionMethodDetails", lang)
        val procurementMethodDetails = getPmd(pmd, country)
        val eligibilityCriteria = getTranslate("eligibilityCriteria", lang)

        val cpvKey = request.tender.classification.id
            .let { CpvKey(it, language) }
        val maybeCpvEntity = cpvRepository.findById(cpvKey)
        val cpvEntity = maybeCpvEntity.orElseThrow { throw InErrorException(ErrorType.INVALID_CPV) }

        val updatedClassification = request.tender.classification.copy(
            description = cpvEntity.name,
            scheme = ClassificationScheme.CPV.value()
        )

        organizationDataService.processOrganization(request.tender.procuringEntity, country)

        val enrichedAp = request.copy(
            tender = request.tender.copy(
                submissionMethodRationale = submissionMethodRationale,
                submissionMethodDetails = submissionMethodDetails,
                procurementMethodDetails = procurementMethodDetails,
                eligibilityCriteria = eligibilityCriteria,
                classification = updatedClassification
            )
        )

        return getResponseDto(data = enrichedAp, id = cm.id)
    }

    companion object UpdateAP {

        fun updateClassification(classification: ClassificationTD, cpv: Cpv): ClassificationTD =
            classification.copy(
                id = cpv.cpvKey?.code!!,
                description = cpv.name,
                scheme = ClassificationScheme.CPV.value()
            )

        fun updateCpv(
            items: List<UpdateAggregationPlan.Tender.Item>,
            language: Language,
            cpvRepository: CpvRepository
        ): List<UpdateAggregationPlan.Tender.Item> {
            val cpvCodes = items.map { it.classification.id }
            val updatedItems =
                if (cpvCodes.isNotEmpty()) {
                    val cpvKeys = cpvCodes.asSequence()
                        .map { CpvKey(it, language) }
                        .toSet()

                    val cpvEntities = cpvRepository.findAllById(cpvKeys)
                    if (cpvEntities.isEmpty() || cpvEntities.size != cpvKeys.size) throw InErrorException(ErrorType.INVALID_CPV)
                    cpvEntities
                        .asSequence()
                        .flatMap { entity ->
                            items.asSequence()
                                .filter { it.classification.id == entity.cpvKey?.code }
                                .map { item ->
                                    item.copy(
                                        classification = item.classification.copy(
                                            scheme = ClassificationScheme.CPV.value(),
                                            description = entity.name
                                        )
                                    )
                                }
                        }
                        .toList()
                } else {
                    items
                }

            return updatedItems
        }

        fun updateCpvs(
            items: List<UpdateAggregationPlan.Tender.Item>,
            language: Language,
            cpvsRepository: CpvsRepository
        ): List<UpdateAggregationPlan.Tender.Item> {

            val cpvsCodes = items.flatMap { item ->
                item.additionalClassifications
                    ?.map { additionalClassification -> additionalClassification.id }
                    .orEmpty()
            }

            val updatedItems =
                if (cpvsCodes.isNotEmpty()) {
                    val cpvsKeys = cpvsCodes.asSequence()
                        .map { CpvsKey(it, language) }
                        .toSet()

                    val cpvsEntities = cpvsRepository.findAllById(cpvsKeys)
                    if (cpvsEntities.isEmpty() || cpvsEntities.size != cpvsKeys.size) throw InErrorException(ErrorType.INVALID_CPVS)
                    cpvsEntities.asSequence()
                        .flatMap { entity ->
                            items.asSequence()
                                .map { item ->
                                    val updatedAC = item.additionalClassifications?.asSequence()
                                        ?.filter { it.id == entity.cpvsKey?.code }
                                        ?.map { additionalClassification ->
                                            additionalClassification.copy(
                                                scheme = ClassificationScheme.CPVS.value(),
                                                description = entity.name
                                            )
                                        }
                                        ?.toList()
                                        .orEmpty()

                                    item.copy(additionalClassifications = updatedAC)
                                }
                        }
                        .toList()
                } else {
                    items
                }

            return updatedItems
        }

        fun updateUnit(
            items: List<UpdateAggregationPlan.Tender.Item>,
            language: Language,
            unitRepository: UnitRepository
        ): List<UpdateAggregationPlan.Tender.Item> {

            val unitCodes = items.map { it.unit.id }
            val updatedItems =
                if (unitCodes.isNotEmpty()) {
                    val unitKeys = unitCodes.asSequence()
                        .map { UnitKey(it, language) }
                        .toSet()

                    val unitEntities = unitRepository.findAllById(unitKeys)
                    if (unitEntities.isEmpty() || unitEntities.size != unitKeys.size) throw InErrorException(ErrorType.INVALID_UNIT)
                    unitEntities.asSequence()
                        .flatMap { entity ->
                            items.asSequence()
                                .filter { it.unit.id == entity.unitKey?.code }
                                .map { item ->
                                    item.copy(
                                        unit = item.unit.copy(
                                            name = entity.name
                                        )
                                    )
                                }
                        }
                        .toList()
                } else {
                    items
                }

            return updatedItems
        }

        fun getUpdatedAddress(
            address: UpdateAggregationPlan.Tender.Address,
            country: Country,
            localityRepository: LocalityRepository,
            regionRepository: RegionRepository
        ): UpdateAggregationPlan.Tender.Address = address.copy(
            addressDetails = address.addressDetails.let { addressDetails ->
                val updatedCountry = getUpdatedCountry(addressDetails.country, country)
                val regionEntity = regionRepository.findByRegionKeyCodeAndRegionKeyCountry(addressDetails.region.id, country)
                    ?: throw InErrorException(ErrorType.REGION_UNKNOWN)
                val updatedReguion = addressDetails.region.updateBy(regionEntity)
                val updatedLocality = getUpdatedLocality(addressDetails.locality, localityRepository, regionEntity)
                addressDetails.copy(
                    //BR-12.5.1
                    country = updatedCountry,
                    //BR-12.5.2
                    region = updatedReguion,
                    //BR-12.5.3
                    locality = updatedLocality
                )
            }
        )


        fun getUpdatedLocality(
            localityDetails: LocalityDetails?,
            localityRepository: LocalityRepository,
            region: Region
        ): LocalityDetails? {
            val updatedLocality = localityDetails?.let {
                val schemeEntity = localityRepository.findOneByScheme(localityDetails.scheme)
                if (schemeEntity != null) {
                    val localityEntity = localityRepository
                        .findByLocalityKeyCodeAndLocalityKeyRegionAndScheme(localityDetails.id, region, localityDetails.scheme)
                        ?: throw InErrorException(ErrorType.LOCALITY_UNKNOWN)

                    localityDetails.copy(
                        description = localityEntity.name,
                        uri = localityEntity.uri
                    )
                } else
                    localityDetails
            }
            return updatedLocality
        }

        fun RegionDetails.updateBy(regionEntity: Region): RegionDetails =
            this.copy(
                scheme = regionEntity.scheme,
                description = regionEntity.name,
                uri = regionEntity.uri
            )

        fun getUpdatedCountry(countryDetails: CountryDetails, country: Country): CountryDetails {
            if (countryDetails.id != country.countryKey?.code) throw InErrorException(ErrorType.INVALID_COUNTRY)

            return countryDetails.copy(
                scheme = country.scheme,
                description = country.name,
                uri = country.uri
            )
        }

    }

    override fun enrichDataForUpdateAP(cm: CommandMessage): ResponseDto {
        val lang = cm.context.language
        val language = validationService.getLanguage(languageCode = lang, internal = true)
        val country = validationService.getCountry(languageCode = lang, countryCode = cm.context.country)
        val request = toObject(UpdateAggregationPlan::class.java, cm.data)

        val (updatedClassification: ClassificationTD?, mainProcurementCategory: String?) = request.tender.classification
            ?.let {
                val commonClass = it.id
                val cpvEntity = cpvRepository.getCommonClass(code = commonClass, language = language)
                    ?: throw InErrorException(ErrorType.INVALID_COMMON_CPV, commonClass)

                val updateClassification = updateClassification(it, cpvEntity)
                val mainProcurementCategory = cpvEntity.mainProcurementCategory
                updateClassification to mainProcurementCategory
            }
            ?: null to null

        val updatedItems = request.tender.items.orEmpty().let { items ->
            // BR-12.1.2
            val updatedItemsCpv = updateCpv(items, language, cpvRepository)
            // BR-12.1.1
            val updatedItemsCpvs = updateCpvs(updatedItemsCpv, language, cpvsRepository)
            // BR-12.1.2
            val updatedItemsUnit = updateUnit(updatedItemsCpvs, language, unitRepository)

            updatedItemsUnit.map { item ->
                val updatedDeliveryAddress = item.deliveryAddress
                    ?.let { getUpdatedAddress(it, country, localityRepository, regionRepository) }
                item.copy(deliveryAddress = updatedDeliveryAddress)
            }
        }

        val updatedLots = request.tender.lots.orEmpty()
            .map { lot ->
                val updatedPlaceOfPerformance = lot.placeOfPerformance?.let {
                    val updatedAddress = getUpdatedAddress(it.address, country, localityRepository, regionRepository)
                    it.copy(address = updatedAddress)
                }
                lot.copy(placeOfPerformance = updatedPlaceOfPerformance)
            }


        val enrichedAp = request.copy(
            tender = request.tender.copy(
                classification = updatedClassification,
                mainProcurementCategory = mainProcurementCategory,
                items = updatedItems,
                lots = updatedLots
            )
        )

        return getResponseDto(data = enrichedAp, id = cm.id)
    }

    private fun processTranslate(dto: TD, country: Country, lang: String, pmd: String) {
        dto.tender.apply {
            submissionMethodRationale = listOf(getTranslate("submissionMethodRationale", lang))
            submissionMethodDetails = getTranslate("submissionMethodDetails", lang)
            procurementMethodDetails = getPmd(pmd, country)
            eligibilityCriteria = getTranslate("eligibilityCriteria", lang)
        }
    }

    private fun processItems(dto: TD, language: Language) {
        //tender.classification
        if (dto.tender.classification != null) {
            val commonClass = dto.tender.classification!!.id
            val cpvEntity = cpvRepository.getCommonClass(code = commonClass, language = language)
                ?: throw InErrorException(ErrorType.INVALID_COMMON_CPV, commonClass)

            dto.tender.classification?.apply {
                id = cpvEntity.cpvKey?.code!!
                description = cpvEntity.name
                scheme = ClassificationScheme.CPV.value()
            }
            dto.tender.apply {
                mainProcurementCategory = cpvEntity.mainProcurementCategory
            }
        }

        val items = dto.tender.items
        //data cpv
        if (items != null) {
            val cpvCodes = getCpvCodes(items)
            if (cpvCodes.isNotEmpty()) {
                val cpvKeys = cpvCodes.asSequence().map { CpvKey(it, language) }.toHashSet()
                val cpvEntities = cpvRepository.findAllById(cpvKeys)
                if (cpvEntities.isEmpty() || cpvEntities.size != cpvKeys.size) throw InErrorException(ErrorType.INVALID_CPV)
                cpvEntities.asSequence().forEach { entity ->
                    items.asSequence()
                        .filter { it.classification.id == entity.cpvKey?.code }
                        .forEach { setCpvData(it.classification, entity) }
                }

                val additionalProcurementCategories = cpvEntities.asSequence()
                    .map { it.mainProcurementCategory }
                    .filter { category -> category != dto.tender.mainProcurementCategory}
                    .toList()

                if (additionalProcurementCategories.isNotEmpty())
                    dto.tender.additionalProcurementCategories = additionalProcurementCategories

            }
            //data cpvs
            val cpvsCodes = getCpvsCodes(items)
            if (cpvsCodes.isNotEmpty()) {
                val cpvsKeys = cpvsCodes.asSequence().map { CpvsKey(it, language) }.toHashSet()
                val cpvsEntities = cpvsRepository.findAllById(cpvsKeys)
                if (cpvsEntities.isEmpty() || cpvsEntities.size != cpvsKeys.size) throw InErrorException(ErrorType.INVALID_CPVS)
                cpvsEntities.asSequence().forEach { entity ->
                    items.asSequence().forEach { item ->
                        item.additionalClassifications?.asSequence()
                            ?.filter { it.id == entity.cpvsKey?.code }
                            ?.forEach { setCpvsData(it, entity) }
                    }
                }
            }
            //data unit
            val unitCodes = getUnitCodes(items)
            if (unitCodes.isNotEmpty()) {
                val unitKeys = unitCodes.asSequence().map { UnitKey(it, language) }.toHashSet()
                val unitEntities = unitRepository.findAllById(unitKeys)
                if (unitEntities.isEmpty() || unitEntities.size != unitKeys.size) throw InErrorException(ErrorType.INVALID_UNIT)
                unitEntities.asSequence().forEach { entity ->
                    items.asSequence()
                        .filter { it.unit.id == entity.unitKey?.code }
                        .forEach { seUnitData(it.unit, entity) }
                }
            }
        }
    }

    private fun getTranslate(code: String, lang: String): String {
        val entity = translateRepository.findByTranslateKeyCodeAndTranslateKeyLanguageCode(
            code = code, languageCode = lang)
            ?: throw InErrorException(ErrorType.TRANSLATION_UNKNOWN, code)
        return entity.name
    }

    private fun getPmd(code: String, country: Country): String {
        val entity = pmdRepository.findByPmdKeyCodeAndPmdKeyCountry(code = code, country = country)
            ?: throw InErrorException(ErrorType.PMD_UNKNOWN, code)
        return entity.name
    }

    private fun getCpvCodes(items: List<ItemTD>): List<String> {
        return items.asSequence().map { it.classification.id }.toList()
    }

    private fun getUnitCodes(items: List<ItemTD>): List<String> {
        return items.asSequence().map { it.unit.id }.toList()
    }

    private fun getCpvsCodes(items: List<ItemTD>): List<String> {
        val cpvsCodes = arrayListOf<String>()
        items.forEach { item ->
            item.additionalClassifications?.let {
                item.additionalClassifications.forEach { ac ->
                    cpvsCodes.add(ac.id)
                }
            }
        }
        return cpvsCodes
    }

    private fun setCpvData(classification: ClassificationTD, entity: Cpv) {
        classification.scheme = ClassificationScheme.CPV.value()
        classification.description = entity.name
    }

    private fun setCpvsData(classification: ClassificationTD, entity: Cpvs) {
        classification.scheme = ClassificationScheme.CPVS.value()
        classification.description = entity.name
    }

    private fun seUnitData(unit: ItemUnitTD, entity: Units) {
        unit.name = entity.name
    }

    private fun getData(cm: CommandMessage): TD {
        if (cm.data.size() == 0) throw InErrorException(ErrorType.INVALID_DATA, null, cm.id)
        val data = toObject(TD::class.java, cm.data)
        val persones = data.tender.procuringEntity?.persones
        if (persones != null && persones.isEmpty())
            throw InErrorException(error = ErrorType.EMPTY_PERSONES, id = cm.id)
        return data
    }
}