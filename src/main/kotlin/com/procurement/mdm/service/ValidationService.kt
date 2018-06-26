package com.procurement.mdm.service

import com.procurement.mdm.exception.ErrorType
import com.procurement.mdm.exception.ExternalErrorException
import com.procurement.mdm.exception.InternalErrorException
import com.procurement.mdm.model.entity.Country
import com.procurement.mdm.model.entity.UnitClass
import com.procurement.mdm.repository.*
import org.springframework.stereotype.Service

interface ValidationService {

    fun checkLanguage(languageCode: String, internal: Boolean)

    fun getCountry(languageCode: String, countryCode: String, internal: Boolean): Country

    fun getUnitClass(languageCode: String, code: String, internal: Boolean): UnitClass

    fun checkEntityKind(code: String, internal: Boolean)

    fun checkCpvParent(parentCode: String, languageCode: String, internal: Boolean)
}

@Service
class ValidationServiceImpl(private val languageRepository: LanguageRepository,
                            private val countryRepository: CountryRepository,
                            private val unitClassRepository: UnitClassRepository,
                            private val entityKindRepository: EntityKindRepository,
                            private val cpvRepository: CpvRepository
) : ValidationService {

    override fun checkLanguage(languageCode: String, internal: Boolean) {
        languageRepository.findByCode(code = languageCode) ?: if (internal) {
            throw InternalErrorException(ErrorType.LANG_UNKNOWN)
        } else {
            throw ExternalErrorException(ErrorType.LANG_UNKNOWN)
        }
    }

    override fun getCountry(languageCode: String, countryCode: String, internal: Boolean): Country {
        languageRepository.findByCode(code = languageCode) ?: if (internal) {
            throw InternalErrorException(ErrorType.LANG_UNKNOWN)
        } else {
            throw ExternalErrorException(ErrorType.LANG_UNKNOWN)
        }
        return countryRepository.findByCountryKeyCodeAndCountryKeyLanguageCode(
                code = countryCode, languageCode = languageCode) ?: if (internal) {
            throw InternalErrorException(ErrorType.COUNTRY_UNKNOWN)
        } else {
            throw ExternalErrorException(ErrorType.COUNTRY_UNKNOWN)
        }
    }

    override fun getUnitClass(languageCode: String, code: String, internal: Boolean): UnitClass {
        languageRepository.findByCode(code = languageCode) ?: if (internal) {
            throw InternalErrorException(ErrorType.LANG_UNKNOWN)
        } else {
            throw ExternalErrorException(ErrorType.LANG_UNKNOWN)
        }
        return unitClassRepository.findByUnitClassKeyCodeAndUnitClassKeyLanguageCode(
                code = code, languageCode = languageCode) ?: if (internal) {
            throw InternalErrorException(ErrorType.UNIT_CLASS_UNKNOWN)
        } else {
            throw ExternalErrorException(ErrorType.UNIT_CLASS_UNKNOWN)
        }
    }

    override fun checkEntityKind(code: String, internal: Boolean) {
        entityKindRepository.findByCode(code) ?: if (internal) {
            throw InternalErrorException(ErrorType.ENTITY_KIND_UNKNOWN)
        } else {
            throw ExternalErrorException(ErrorType.ENTITY_KIND_UNKNOWN)
        }
    }

    override fun checkCpvParent(parentCode: String, languageCode: String, internal: Boolean) {
        cpvRepository.findByCpvKeyCodeAndCpvKeyLanguageCode(code = parentCode, languageCode = languageCode)
                ?: if (internal) {
                    throw InternalErrorException(ErrorType.CPV_CODE_UNKNOWN)
                } else {
                    throw ExternalErrorException(ErrorType.CPV_CODE_UNKNOWN)
                }
    }
}


