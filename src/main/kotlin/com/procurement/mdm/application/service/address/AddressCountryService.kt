package com.procurement.mdm.application.service.address

import com.procurement.mdm.application.exception.CountryNotFoundException
import com.procurement.mdm.domain.model.code.CountryCode
import com.procurement.mdm.domain.model.code.LanguageCode
import com.procurement.mdm.domain.model.identifier.CountryIdentifier
import com.procurement.mdm.domain.repository.AdvancedLanguageRepository
import com.procurement.mdm.domain.repository.address.AddressCountryRepository
import org.springframework.stereotype.Service

interface AddressCountryService {
    fun getAll(language: String): List<CountryIdentifier>

    fun getBy(country: String, language: String): CountryIdentifier
}

@Service
class AddressCountryServiceImpl(
    private val addressCountryRepository: AddressCountryRepository,
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

    override fun getBy(country: String, language: String): CountryIdentifier {
        val countryCode = CountryCode(country)
        val languageCode = LanguageCode(language)
            .apply {
                validation(advancedLanguageRepository)
            }

        val countryEntity = addressCountryRepository.findBy(country = countryCode, language = languageCode)
            ?: throw CountryNotFoundException(country = country, language = language)

        return CountryIdentifier(
            scheme = countryEntity.scheme,
            id = countryEntity.id,
            description = countryEntity.description,
            uri = countryEntity.uri
        )
    }
}
