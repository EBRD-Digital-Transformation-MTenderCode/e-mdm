package com.procurement.mdm.service

import com.procurement.mdm.exception.ErrorType
import com.procurement.mdm.exception.ExternalErrorException
import com.procurement.mdm.exception.InternalErrorException
import com.procurement.mdm.repository.CountryRepository
import com.procurement.mdm.repository.EntityKindRepository
import com.procurement.mdm.repository.LanguageRepository
import com.procurement.mdm.repository.UnitClassRepository
import org.springframework.stereotype.Service

interface ValidationService {

    fun lang(lang: String, internal: Boolean)

    fun country(lang: String, country: String, internal: Boolean)

    fun unitClass(code: String, internal: Boolean)

    fun entityKind(code: String, internal: Boolean)
}

@Service
class ValidationServiceImpl(private val languageRepository: LanguageRepository,
                            private val countryRepository: CountryRepository,
                            private val unitClassRepository: UnitClassRepository,
                            private val entityKindRepository: EntityKindRepository) : ValidationService {

    override fun lang(lang: String, internal: Boolean) {
        languageRepository.findByCode(lang) ?: if (internal) {
            throw InternalErrorException(ErrorType.LANG_UNKNOWN)
        } else {
            throw ExternalErrorException(ErrorType.LANG_UNKNOWN)
        }
    }

    override fun country(lang: String, country: String, internal: Boolean) {
        languageRepository.findByCode(lang) ?: if (internal) {
            throw InternalErrorException(ErrorType.LANG_UNKNOWN)
        } else {
            throw ExternalErrorException(ErrorType.LANG_UNKNOWN)
        }
        countryRepository.findByCodeAndLanguageCode(country, lang) ?: if (internal) {
            throw InternalErrorException(ErrorType.COUNTRY_UNKNOWN)
        } else {
            throw ExternalErrorException(ErrorType.COUNTRY_UNKNOWN)
        }
    }

    override fun unitClass(code: String, internal: Boolean) {
        unitClassRepository.findByCode(code) ?: if (internal) {
            throw InternalErrorException(ErrorType.UNIT_CLASS_UNKNOWN)
        } else {
            throw ExternalErrorException(ErrorType.UNIT_CLASS_UNKNOWN)
        }
    }

    override fun entityKind(code: String, internal: Boolean) {
        entityKindRepository.findByCode(code) ?: if (internal) {
            throw InternalErrorException(ErrorType.ENTITY_KIND_UNKNOWN)
        } else {
            throw ExternalErrorException(ErrorType.ENTITY_KIND_UNKNOWN)
        }
    }
}
