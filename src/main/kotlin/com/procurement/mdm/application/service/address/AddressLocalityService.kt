package com.procurement.mdm.application.service.address

import com.procurement.mdm.application.exception.LocalityNotFoundException
import com.procurement.mdm.application.exception.LocalityNotLinkedToRegionException
import com.procurement.mdm.application.exception.RegionNotLinkedToCountryException
import com.procurement.mdm.domain.model.code.CountryCode
import com.procurement.mdm.domain.model.code.LanguageCode
import com.procurement.mdm.domain.model.code.LocalityCode
import com.procurement.mdm.domain.model.code.RegionCode
import com.procurement.mdm.domain.model.identifier.LocalityIdentifier
import com.procurement.mdm.domain.model.scheme.LocalityScheme
import com.procurement.mdm.domain.repository.AdvancedLanguageRepository
import com.procurement.mdm.domain.repository.address.AddressLocalityRepository
import com.procurement.mdm.domain.repository.scheme.LocalitySchemeRepository
import com.procurement.mdm.domain.repository.scheme.RegionSchemeRepository
import org.springframework.stereotype.Service

interface AddressLocalityService {
    fun getBy(locality: String, country: String, region: String, language: String, scheme: String?): LocalityIdentifier

    fun getBy(country: String, region: String, language: String): List<LocalityIdentifier>

    fun getAllSchemes(country: String, region: String): List<String>
}

@Service
class AddressLocalityServiceImpl(
    private val addressLocalityRepository: AddressLocalityRepository,
    private val localitySchemeRepository: LocalitySchemeRepository,
    private val regionSchemeRepository: RegionSchemeRepository,
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
        language: String,
        scheme: String?
    ): LocalityIdentifier {

        val localityCode = LocalityCode(locality)
        val countryCode = CountryCode(country)
        val regionCode = RegionCode(region)
        val languageCode = LanguageCode(language)

        return if (scheme == null)
            getByDefaultScheme(localityCode, countryCode, regionCode, languageCode)
        else
            getByReceivedScheme(localityCode, countryCode, regionCode, languageCode, scheme)
    }

    private fun getByDefaultScheme(
        locality: LocalityCode, country: CountryCode, region: RegionCode, language: LanguageCode
    ): LocalityIdentifier {
        language.apply {
            validation(advancedLanguageRepository)
        }

        val localityEntity = addressLocalityRepository.findBy(
            locality = locality, country = country, region = region, language = language
        ) ?: throw LocalityNotFoundException(
            locality = locality, country = country, region = region, language = language
        )

        return LocalityIdentifier(
            scheme = localityEntity.scheme,
            id = localityEntity.id,
            description = localityEntity.description,
            uri = localityEntity.uri
        )
    }

    private fun getByReceivedScheme(
        locality: LocalityCode, country: CountryCode, region: RegionCode, language: LanguageCode, scheme: String
    ): LocalityIdentifier {
        val localityScheme = LocalityScheme(scheme).apply {
            validation(localitySchemeRepository)
        }
        checkExists(locality = locality, scheme = localityScheme)
        checkRegionCode(locality = locality, scheme = localityScheme, region = region)
        checkCountryCode(region = region, country = country)

        val localityEntity = localitySchemeRepository.findBy(
            locality = locality, scheme = localityScheme, region = region, language = language
        ) ?: throw LocalityNotFoundException(
            locality = locality, scheme = localityScheme, country = country, region = region, language = language
        )

        return LocalityIdentifier(
            scheme = localityEntity.scheme,
            id = localityEntity.id,
            description = localityEntity.description,
            uri = localityEntity.uri
        )
    }

    private fun checkExists(locality: LocalityCode, scheme: LocalityScheme) {
        if (localitySchemeRepository.existsBy(locality = locality, scheme = scheme).not())
            throw LocalityNotFoundException(locality = locality, scheme = scheme)
    }

    private fun checkRegionCode(locality: LocalityCode, scheme: LocalityScheme, region: RegionCode) {
        if (localitySchemeRepository.existsBy(locality = locality, scheme = scheme, region = region).not())
            throw LocalityNotLinkedToRegionException(locality = locality, scheme = scheme, region = region)
    }

    private fun checkCountryCode(region: RegionCode, country: CountryCode) {
        if (regionSchemeRepository.existsBy(region = region, country = country).not())
            throw RegionNotLinkedToCountryException(region = region, country = country)
    }

    override fun getAllSchemes(country: String, region: String): List<String> {
        val countryCode = CountryCode(country)
        val regionCode = RegionCode(region)

        return addressLocalityRepository.findAllSchemes(country = countryCode, region = regionCode)
    }
}
