package com.procurement.mdm.application.service.address

import com.procurement.mdm.application.exception.CountryNotFoundException
import com.procurement.mdm.application.exception.IdNotFoundException
import com.procurement.mdm.domain.model.code.CountryCode
import com.procurement.mdm.domain.model.code.LanguageCode
import com.procurement.mdm.domain.model.identifier.CountryIdentifier
import com.procurement.mdm.domain.model.scheme.CountryScheme
import com.procurement.mdm.domain.repository.AdvancedLanguageRepository
import com.procurement.mdm.domain.repository.address.AddressCountryRepository
import com.procurement.mdm.domain.repository.scheme.CountrySchemeRepository
import org.springframework.stereotype.Service

interface AddressCountryService {
    fun getAll(language: String): List<CountryIdentifier>

    fun getBy(country: String, language: String, scheme: String?): CountryIdentifier
}

@Service
class AddressCountryServiceImpl(
    private val addressCountryRepository: AddressCountryRepository,
    private val countrySchemeRepository: CountrySchemeRepository,
    private val advancedLanguageRepository: AdvancedLanguageRepository
) : AddressCountryService {

    override fun getAll(language: String): List<CountryIdentifier> {
        val languageCode = LanguageCode(language)
            .apply {
                validation(advancedLanguageRepository)
            }

        val countriesEntities = addressCountryRepository.findAll(language = languageCode)
        if (countriesEntities.isEmpty())
            throw CountryNotFoundException(language = language)

        return countriesEntities.map {
            CountryIdentifier(
                scheme = it.scheme,
                id = it.id,
                description = it.description,
                uri = it.uri
            )
        }
    }

    override fun getBy(country: String, language: String, scheme: String?): CountryIdentifier {
        val countryCode = CountryCode(country)
        val languageCode = LanguageCode(language)

        return if (scheme == null)
            getByDefaultScheme(countryCode, languageCode)
        else
            getByReceivedScheme(countryCode, languageCode, scheme)
    }

    private fun getByDefaultScheme(country: CountryCode, language: LanguageCode): CountryIdentifier {
        language.apply {
            validation(advancedLanguageRepository)
        }

        val countryEntity = addressCountryRepository.findBy(country = country, language = language)
            ?: throw CountryNotFoundException(country = country, language = language)

        return CountryIdentifier(
            scheme = countryEntity.scheme,
            id = countryEntity.id,
            description = countryEntity.description,
            uri = countryEntity.uri
        )
    }

    private fun getByReceivedScheme(country: CountryCode, language: LanguageCode, scheme: String): CountryIdentifier {
        val countryScheme = CountryScheme(scheme).apply {
            validation(countrySchemeRepository)
        }

        checkExists(scheme = countryScheme, country = country)

        val countryEntity = countrySchemeRepository.findBy(
            country = country, language = language, scheme = countryScheme
        )
            ?: throw CountryNotFoundException(country = country, language = language)

        return CountryIdentifier(
            scheme = countryEntity.scheme,
            id = countryEntity.id,
            description = countryEntity.description,
            uri = countryEntity.uri
        )
    }

    private fun checkExists(scheme: CountryScheme, country: CountryCode) {
        if (countrySchemeRepository.existsBy(scheme, country).not())
            throw IdNotFoundException(scheme = scheme, country = country)
    }
}
