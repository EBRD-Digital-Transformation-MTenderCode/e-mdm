package com.procurement.mdm.application.service.address

import com.procurement.mdm.application.exception.IdNotFoundException
import com.procurement.mdm.application.exception.RegionNotFoundException
import com.procurement.mdm.application.exception.RegionNotLinkedToCountryException
import com.procurement.mdm.domain.model.code.CountryCode
import com.procurement.mdm.domain.model.code.LanguageCode
import com.procurement.mdm.domain.model.code.RegionCode
import com.procurement.mdm.domain.model.identifier.RegionIdentifier
import com.procurement.mdm.domain.model.scheme.RegionScheme
import com.procurement.mdm.domain.repository.AdvancedLanguageRepository
import com.procurement.mdm.domain.repository.address.AddressRegionRepository
import com.procurement.mdm.domain.repository.scheme.RegionSchemeRepository
import org.springframework.stereotype.Service

interface AddressRegionService {
    fun getBy(region: String, country: String, language: String, scheme: String?): RegionIdentifier
}

@Service
class AddressRegionServiceImpl(
    private val addressRegionRepository: AddressRegionRepository,
    private val regionSchemeRepository: RegionSchemeRepository,
    private val advancedLanguageRepository: AdvancedLanguageRepository
) : AddressRegionService {

    override fun getBy(region: String, country: String, language: String, scheme: String?): RegionIdentifier {
        val countryCode = CountryCode(country)
        val regionCode = RegionCode(region)
        val languageCode = LanguageCode(language)

        return if (scheme == null)
            getByDefaultScheme(countryCode, regionCode, languageCode)
        else
            getByReceivedScheme(countryCode, regionCode, languageCode, scheme)
    }

    private fun getByDefaultScheme(
        country: CountryCode, region: RegionCode, language: LanguageCode
    ): RegionIdentifier {
        language.apply {
            validation(advancedLanguageRepository)
        }

        val regionEntity = addressRegionRepository.findBy(
            region = region,
            country = country,
            language = language
        ) ?: throw RegionNotFoundException(region = region, country = country, language = language)

        return RegionIdentifier(
            scheme = regionEntity.scheme,
            id = regionEntity.id,
            description = regionEntity.description,
            uri = regionEntity.uri
        )
    }

    private fun getByReceivedScheme(
        country: CountryCode, region: RegionCode, language: LanguageCode, scheme: String
    ): RegionIdentifier {
        val regionScheme = RegionScheme(scheme).apply {
            validation(regionSchemeRepository)
        }

        checkExists(region = region, scheme = regionScheme)
        checkCountryCode(region = region, scheme = regionScheme, country = country)

        val regionEntity = regionSchemeRepository.findBy(
            region = region, scheme = regionScheme, language = language, country = country
        )
            ?: throw RegionNotFoundException(
                region = region, scheme = regionScheme, country = country, language = language
            )

        return RegionIdentifier(
            scheme = regionEntity.scheme,
            id = regionEntity.id,
            description = regionEntity.description,
            uri = regionEntity.uri
        )
    }

    private fun checkExists(region: RegionCode, scheme: RegionScheme) {
        if (regionSchemeRepository.existsBy(region = region, scheme = scheme).not())
            throw IdNotFoundException(region = region, scheme = scheme)
    }

    private fun checkCountryCode(region: RegionCode, scheme: RegionScheme, country: CountryCode) {
        if (regionSchemeRepository.existsBy(region = region, scheme = scheme, country = country).not())
            throw RegionNotLinkedToCountryException(region = region, scheme = scheme, country = country)
    }
}
