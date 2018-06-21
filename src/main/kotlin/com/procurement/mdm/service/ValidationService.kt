package com.procurement.mdm.service

import com.procurement.mdm.exception.ErrorType
import com.procurement.mdm.exception.ExternalErrorException
import com.procurement.mdm.exception.InternalErrorException
import com.procurement.mdm.repository.*
import org.springframework.stereotype.Service

interface ValidationService {

    fun lang(lang: String, internal: Boolean)

    fun country(lang: String, country: String, internal: Boolean)

    fun unitClass(code: String, internal: Boolean)

    fun entityKind(code: String, internal: Boolean)

    fun cpvCode(code: String, internal: Boolean)

    fun cpvsCode(code: String, internal: Boolean)
}

@Service
class ValidationServiceImpl(private val languageRepository: LanguageRepository,
                            private val countryRepository: CountryRepository,
                            private val unitClassRepository: UnitClassRepository,
                            private val entityKindRepository: EntityKindRepository,
                            private val cpvRepository: CPVRepository,
                            private val cpvsRepository: CPVsRepository) : ValidationService {

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

    override fun cpvCode(code: String, internal: Boolean) {
        cpvRepository.findByCode(code) ?: if (internal) {
            throw InternalErrorException(ErrorType.CPV_CODE_UNKNOWN)
        } else {
            throw ExternalErrorException(ErrorType.CPV_CODE_UNKNOWN)
        }
    }

    override fun cpvsCode(code: String, internal: Boolean) {
        cpvsRepository.findByCode(code) ?: if (internal) {
            throw InternalErrorException(ErrorType.CPVS_CODE_UNKNOWN)
        } else {
            throw ExternalErrorException(ErrorType.CPVS_CODE_UNKNOWN)
        }
    }
}


