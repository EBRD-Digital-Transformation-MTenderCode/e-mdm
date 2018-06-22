package com.procurement.mdm.service

import com.procurement.mdm.exception.ErrorType
import com.procurement.mdm.exception.ExternalErrorException
import com.procurement.mdm.exception.InternalErrorException
import com.procurement.mdm.repository.CPVRepository
import com.procurement.mdm.repository.LanguageRepository
import org.springframework.stereotype.Service

interface ValidationService {

    fun checkLanguage(languageCode: String, internal: Boolean)

//    fun getCountryId(languageCode: String, countryCode: String, internal: Boolean): String
//
//    fun getUnitClassId(code: String, internal: Boolean): String
//
//    fun getEntityKindId(code: String, internal: Boolean): String

    fun checkCpvParent(parentCode: String, languageCode: String, internal: Boolean)

//    fun checkCpvsParent(parentCode: String, languageId: String, internal: Boolean): String
}

@Service
class ValidationServiceImpl(private val languageRepository: LanguageRepository,
//                            private val countryRepository: CountryRepository,
//                            private val unitClassRepository: UnitClassRepository,
//                            private val entityKindRepository: EntityKindRepository,
//                            private val cpvsRepository: CPVsRepository,
                            private val cpvRepository: CPVRepository
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
//
//    override fun getUnitClassId(code: String, internal: Boolean): String {
//        val unitClassEntity = unitClassRepository.findByCode(code) ?: if (internal) {
//            throw InternalErrorException(ErrorType.UNIT_CLASS_UNKNOWN)
//        } else {
//            throw ExternalErrorException(ErrorType.UNIT_CLASS_UNKNOWN)
//        }
//        return unitClassEntity.id
//    }
//
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

//    override fun checkCpvsParent(parentCode: String, languageId: String, internal: Boolean): String {
//        val entity = cpvsRepository.findByLanguageIdAndCode(languageId, parentCode) ?: if (internal) {
//            throw InternalErrorException(ErrorType.CPVS_CODE_UNKNOWN)
//        } else {
//            throw ExternalErrorException(ErrorType.CPVS_CODE_UNKNOWN)
//        }
//        return entity.id
//    }
}


