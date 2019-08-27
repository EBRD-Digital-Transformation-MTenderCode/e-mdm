package com.procurement.mdm.application.service.address

import com.procurement.mdm.application.exception.RegionNotFoundException
import com.procurement.mdm.domain.model.code.CountryCode
import com.procurement.mdm.domain.model.code.LanguageCode
import com.procurement.mdm.domain.model.code.RegionCode
import com.procurement.mdm.domain.model.identifier.RegionIdentifier
import com.procurement.mdm.domain.repository.AdvancedLanguageRepository
import com.procurement.mdm.domain.repository.address.AddressRegionRepository
import org.springframework.stereotype.Service

interface AddressRegionService {
    fun getBy(region: String, country: String, language: String): RegionIdentifier
}

@Service
class AddressRegionServiceImpl(
    private val addressRegionRepository: AddressRegionRepository,
    private val advancedLanguageRepository: AdvancedLanguageRepository
) : AddressRegionService {

    override fun getBy(region: String, country: String, language: String): RegionIdentifier {
        val countryCode = CountryCode(country)
        val regionCode = RegionCode(region)
        val languageCode = LanguageCode(language)
            .apply {
                validation(advancedLanguageRepository)
            }

        val regionEntity = addressRegionRepository.findBy(
            region = regionCode,
            country = countryCode,
            language = languageCode
        ) ?: throw RegionNotFoundException(region = region, country = country, language = language)

        return RegionIdentifier(
            scheme = regionEntity.scheme,
            id = regionEntity.id,
            description = regionEntity.description,
            uri = regionEntity.uri
        )
    }
}
