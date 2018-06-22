package com.procurement.mdm.service

import com.procurement.mdm.exception.ErrorType
import com.procurement.mdm.exception.ExternalErrorException
import com.procurement.mdm.exception.InternalErrorException
import com.procurement.mdm.repository.CpvRepository
import com.procurement.mdm.repository.CpvsRepository
import com.procurement.mdm.repository.LanguageRepository
import com.procurement.mdm.repository.UnitClassRepository
import org.springframework.stereotype.Service

interface ValidationService {

    fun checkLanguage(languageCode: String, internal: Boolean)

//    fun getCountryId(languageCode: String, countryCode: String, internal: Boolean): String

    fun checkUnitClass(code: String, internal: Boolean)
//
//    fun getEntityKindId(code: String, internal: Boolean): String

    fun checkCpvParent(parentCode: String, languageCode: String, internal: Boolean)

    fun checkCpvsParent(parentCode: String, languageCode: String, internal: Boolean)
}

@Service
class ValidationServiceImpl(private val languageRepository: LanguageRepository,
//                            private val countryRepository: CountryRepository,
                            private val unitClassRepository: UnitClassRepository,
//                            private val entityKindRepository: EntityKindRepository,
                            private val cpvsRepository: CpvsRepository,
                            private val cpvRepository: CpvRepository
) : ValidationService {

    override fun checkLanguage(languageCode: String, internal: Boolean) {
        languageRepository.findByCode(languageCode) ?: if (internal) {
            throw InternalErrorException(ErrorType.LANG_UNKNOWN)
        } else {
            throw ExternalErrorException(ErrorType.LANG_UNKNOWN)
        }
    }

//    override fun getCountryId(languageCode: String, countryCode: String, internal: Boolean): String {
//        val languageEntity = languageRepository.findByCode(languageCode) ?: if (internal) {
//            throw InternalErrorException(ErrorType.LANG_UNKNOWN)
//        } else {
//            throw ExternalErrorException(ErrorType.LANG_UNKNOWN)
//        }
//        val countryEntity = countryRepository.findByCodeAndLanguageId(countryCode, languageEntity.id) ?: if (internal) {
//            throw InternalErrorException(ErrorType.COUNTRY_UNKNOWN)
//        } else {
//            throw ExternalErrorException(ErrorType.COUNTRY_UNKNOWN)
//        }
//        return countryEntity.id
//    }

    override fun checkUnitClass(code: String, internal: Boolean) {
        unitClassRepository.findByCode(code = code) ?: if (internal) {
            throw InternalErrorException(ErrorType.UNIT_CLASS_UNKNOWN)
        } else {
            throw ExternalErrorException(ErrorType.UNIT_CLASS_UNKNOWN)
        }
    }

//    override fun getEntityKindId(code: String, internal: Boolean): String {
//        val entityKindEntity = entityKindRepository.findByCode(code) ?: if (internal) {
//            throw InternalErrorException(ErrorType.ENTITY_KIND_UNKNOWN)
//        } else {
//            throw ExternalErrorException(ErrorType.ENTITY_KIND_UNKNOWN)
//        }
//        return entityKindEntity.id
//    }

    override fun checkCpvParent(parentCode: String, languageCode: String, internal: Boolean) {
        cpvRepository.findByCpvKeyCodeAndCpvKeyLanguageCode(code = parentCode, languageCode = languageCode)
                ?: if (internal) {
                    throw InternalErrorException(ErrorType.CPV_CODE_UNKNOWN)
                } else {
                    throw ExternalErrorException(ErrorType.CPV_CODE_UNKNOWN)
                }
    }

    override fun checkCpvsParent(parentCode: String, languageCode: String, internal: Boolean) {
        cpvsRepository.findByCpvsKeyCodeAndCpvsKeyLanguageCode(code = parentCode, languageCode = languageCode)
                ?: if (internal) {
                    throw InternalErrorException(ErrorType.CPV_CODE_UNKNOWN)
                } else {
                    throw ExternalErrorException(ErrorType.CPV_CODE_UNKNOWN)
                }
    }
}


