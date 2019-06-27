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
}

@Service
class AddressLocalityServiceImpl(
    private val addressLocalityRepository: AddressLocalityRepository,
    private val advancedLanguageRepository: AdvancedLanguageRepository
) : AddressLocalityService {

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
}
