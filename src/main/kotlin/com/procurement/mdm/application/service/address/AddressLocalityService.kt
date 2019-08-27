package com.procurement.mdm.application.service.address

import com.procurement.mdm.application.exception.LocalityNotFoundException
import com.procurement.mdm.domain.model.code.CountryCode
import com.procurement.mdm.domain.model.code.LanguageCode
import com.procurement.mdm.domain.model.code.LocalityCode
import com.procurement.mdm.domain.model.code.RegionCode
import com.procurement.mdm.domain.model.identifier.LocalityIdentifier
import com.procurement.mdm.domain.repository.AdvancedLanguageRepository
import com.procurement.mdm.domain.repository.address.AddressLocalityRepository
import org.springframework.stereotype.Service

interface AddressLocalityService {
    fun getBy(locality: String, country: String, region: String, language: String): LocalityIdentifier

    fun getBy(country: String, region: String, language: String): List<LocalityIdentifier>

    fun getAllSchemes(country: String, region: String): List<String>
}

@Service
class AddressLocalityServiceImpl(
    private val addressLocalityRepository: AddressLocalityRepository,
    private val advancedLanguageRepository: AdvancedLanguageRepository
) : AddressLocalityService {

    override fun getBy(country: String, region: String, language: String): List<LocalityIdentifier> {
        val countryCode = CountryCode(country)
        val regionCode = RegionCode(region)
        val languageCode = LanguageCode(language).apply {
            validation(advancedLanguageRepository)
        }

        return addressLocalityRepository.findAll(country = countryCode, region = regionCode, language = languageCode)
            .map { localityEntity ->
                LocalityIdentifier(
                    scheme = localityEntity.scheme,
                    id = localityEntity.id,
                    description = localityEntity.description,
                    uri = localityEntity.uri
                )
            }
    }

    override fun getBy(
        locality: String,
        country: String,
        region: String,
        language: String
    ): LocalityIdentifier {

        val localityCode = LocalityCode(locality)
        val countryCode = CountryCode(country)
        val regionCode = RegionCode(region)
        val languageCode = LanguageCode(language).apply {
            validation(advancedLanguageRepository)
        }

        val localityEntity = addressLocalityRepository.findBy(
            locality = localityCode,
            country = countryCode,
            region = regionCode,
            language = languageCode
        ) ?: throw LocalityNotFoundException(
            locality = locality,
            country = country,
            region = region,
            language = language
        )

        return LocalityIdentifier(
            scheme = localityEntity.scheme,
            id = localityEntity.id,
            description = localityEntity.description,
            uri = localityEntity.uri
        )
    }

    override fun getAllSchemes(country: String, region: String): List<String> {
        val countryCode = CountryCode(country)
        val regionCode = RegionCode(region)

        return addressLocalityRepository.findAllSchemes(country = countryCode, region = regionCode)
    }
}
