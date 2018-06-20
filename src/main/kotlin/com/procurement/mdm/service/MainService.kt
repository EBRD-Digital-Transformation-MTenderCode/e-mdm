package com.procurement.mdm.service

import com.procurement.mdm.exception.ErrorType
import com.procurement.mdm.exception.ExternalErrorException
import com.procurement.mdm.exception.InternalErrorException
import com.procurement.mdm.repository.CountryRepository
import com.procurement.mdm.repository.LanguageRepository
import org.springframework.stereotype.Service

interface MainService {

    fun validateParams(lang: String, country: String?, internal: Boolean)

    fun validateParams(lang: String, internal: Boolean)
}

@Service
class MainServiceImpl(private val languageRepository: LanguageRepository,
                      private val countryRepository: CountryRepository) : MainService {

    override fun validateParams(lang: String, internal: Boolean) {
        languageRepository.findByCode(lang) ?: if (internal) {
            throw InternalErrorException(ErrorType.LANG_UNKNOWN)
        } else {
            throw ExternalErrorException(ErrorType.LANG_UNKNOWN)
        }
    }

    override fun validateParams(lang: String, country: String?, internal: Boolean) {
        languageRepository.findByCode(lang) ?: if (internal) {
            throw InternalErrorException(ErrorType.LANG_UNKNOWN)
        } else {
            throw ExternalErrorException(ErrorType.LANG_UNKNOWN)
        }
        if (country != null) {
            countryRepository.findByCodeAndLanguageCode(country, lang) ?: if (internal) {
                throw InternalErrorException(ErrorType.COUNTRY_UNKNOWN)
            } else {
                throw ExternalErrorException(ErrorType.COUNTRY_UNKNOWN)
            }
        }
    }
}
