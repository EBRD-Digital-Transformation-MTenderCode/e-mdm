package com.procurement.mdm.service

import com.procurement.mdm.exception.ErrorType
import com.procurement.mdm.exception.ExternalErrorException
import com.procurement.mdm.exception.InternalErrorException
import com.procurement.mdm.repository.*
import org.springframework.stereotype.Service

interface ValidationService {

    fun getLanguageId(languageCode: String, internal: Boolean): String

    fun getCountryId(languageCode: String, countryCode: String, internal: Boolean): String

    fun getUnitClassId(code: String, internal: Boolean): String

    fun getEntityKindId(code: String, internal: Boolean): String

    fun getCpvParentId(languageId: String, parentCode: String, internal: Boolean): String

    fun getCpvsParentId(languageId: String, parentCode: String, internal: Boolean): String
}

@Service
class ValidationServiceImpl(private val languageRepository: LanguageRepository,
                            private val countryRepository: CountryRepository,
                            private val unitClassRepository: UnitClassRepository,
                            private val entityKindRepository: EntityKindRepository,
                            private val cpvRepository: CPVRepository,
                            private val cpvsRepository: CPVsRepository) : ValidationService {

    override fun getLanguageId(languageCode: String, internal: Boolean): String {
        val entity = languageRepository.findByCode(languageCode) ?: if (internal) {
            throw InternalErrorException(ErrorType.LANG_UNKNOWN)
        } else {
            throw ExternalErrorException(ErrorType.LANG_UNKNOWN)
        }
        return entity.id
    }

    override fun getCountryId(languageCode: String, countryCode: String, internal: Boolean): String {
        val languageEntity = languageRepository.findByCode(languageCode) ?: if (internal) {
            throw InternalErrorException(ErrorType.LANG_UNKNOWN)
        } else {
            throw ExternalErrorException(ErrorType.LANG_UNKNOWN)
        }
        val countryEntity = countryRepository.findByCodeAndLanguageId(countryCode, languageEntity.id) ?: if (internal) {
            throw InternalErrorException(ErrorType.COUNTRY_UNKNOWN)
        } else {
            throw ExternalErrorException(ErrorType.COUNTRY_UNKNOWN)
        }
        return countryEntity.id
    }

    override fun getUnitClassId(code: String, internal: Boolean): String {
        val unitClassEntity = unitClassRepository.findByCode(code) ?: if (internal) {
            throw InternalErrorException(ErrorType.UNIT_CLASS_UNKNOWN)
        } else {
            throw ExternalErrorException(ErrorType.UNIT_CLASS_UNKNOWN)
        }
        return unitClassEntity.id
    }

    override fun getEntityKindId(code: String, internal: Boolean): String {
        val entityKindEntity = entityKindRepository.findByCode(code) ?: if (internal) {
            throw InternalErrorException(ErrorType.ENTITY_KIND_UNKNOWN)
        } else {
            throw ExternalErrorException(ErrorType.ENTITY_KIND_UNKNOWN)
        }
        return entityKindEntity.id
    }

    override fun getCpvParentId(languageId: String, parentCode: String, internal: Boolean): String {
        val parentEntity = cpvRepository.findByLanguageIdAndCode(languageId, parentCode) ?: if (internal) {
            throw InternalErrorException(ErrorType.CPV_CODE_UNKNOWN)
        } else {
            throw ExternalErrorException(ErrorType.CPV_CODE_UNKNOWN)
        }
        return parentEntity.id
    }

    override fun getCpvsParentId(languageId: String, parentCode: String, internal: Boolean): String {
        val entity = cpvsRepository.findByLanguageIdAndCode(languageId, parentCode) ?: if (internal) {
            throw InternalErrorException(ErrorType.CPVS_CODE_UNKNOWN)
        } else {
            throw ExternalErrorException(ErrorType.CPVS_CODE_UNKNOWN)
        }
        return entity.id
    }
}


