package com.procurement.mdm.service

import com.procurement.mdm.exception.ErrorType
import com.procurement.mdm.exception.ExErrorException
import com.procurement.mdm.exception.InErrorException
import com.procurement.mdm.model.entity.Country
import com.procurement.mdm.model.entity.Language
import com.procurement.mdm.model.entity.Region
import com.procurement.mdm.model.entity.UnitClass
import com.procurement.mdm.repository.*
import org.springframework.stereotype.Service

interface ValidationService {

    fun getLanguage(languageCode: String, internal: Boolean = false): Language

    fun getCountry(languageCode: String, countryCode: String, internal: Boolean = false): Country

    fun getRegion(languageCode: String, countryCode: String, regionCode: String, internal: Boolean = false): Region

    fun getUnitClass(languageCode: String, code: String, internal: Boolean = false): UnitClass

    fun checkEntityKind(code: String, internal: Boolean = false)

    fun checkCpvParent(parentCode: String, languageCode: String, internal: Boolean = false)
}

@Service
class ValidationServiceImpl(private val languageRepository: LanguageRepository,
                            private val countryRepository: CountryRepository,
                            private val regionRepository: RegionRepository,
                            private val unitClassRepository: UnitClassRepository,
                            private val entityKindRepository: EntityKindRepository,
                            private val cpvRepository: CpvRepository
) : ValidationService {

    override fun getLanguage(languageCode: String, internal: Boolean): Language {
        return languageRepository.findByCode(code = languageCode)
                ?: throw errorException(ErrorType.LANG_UNKNOWN, internal)
    }

    override fun getCountry(languageCode: String, countryCode: String, internal: Boolean): Country {
        languageRepository.findByCode(code = languageCode)
                ?: throw errorException(ErrorType.LANG_UNKNOWN, internal)
        return countryRepository.findByCountryKeyLanguageCodeAndCountryKeyCode(languageCode = languageCode, code = countryCode)
                ?: throw errorException(ErrorType.COUNTRY_UNKNOWN, internal)
    }

    override fun getRegion(languageCode: String, countryCode: String, regionCode: String, internal: Boolean): Region {
        languageRepository.findByCode(code = languageCode)
                ?: throw errorException(ErrorType.LANG_UNKNOWN, internal)
        val country = getCountry(languageCode = languageCode, countryCode = countryCode)
        return regionRepository.findByRegionKeyCodeAndRegionKeyCountry(code = regionCode, country = country)
                ?: throw errorException(ErrorType.REGION_UNKNOWN, internal)
    }

    override fun getUnitClass(languageCode: String, code: String, internal: Boolean): UnitClass {
        languageRepository.findByCode(code = languageCode)
                ?: throw errorException(ErrorType.LANG_UNKNOWN, internal)
        return unitClassRepository.findByUnitClassKeyCodeAndUnitClassKeyLanguageCode(code = code, languageCode = languageCode)
                ?: throw errorException(ErrorType.UNIT_CLASS_UNKNOWN, internal)
    }

    override fun checkEntityKind(code: String, internal: Boolean) {
        entityKindRepository.findByCode(code)
                ?: throw errorException(ErrorType.ENTITY_KIND_UNKNOWN, internal)
    }

    override fun checkCpvParent(parentCode: String, languageCode: String, internal: Boolean) {
        cpvRepository.findByCpvKeyCodeAndCpvKeyLanguageCode(code = parentCode, languageCode = languageCode)
                ?: throw errorException(ErrorType.CPV_CODE_UNKNOWN, internal)
    }

    private fun errorException(errorType: ErrorType, internal: Boolean): RuntimeException {
        if (internal) {
            throw InErrorException(errorType)
        } else {
            throw ExErrorException(errorType)
        }
    }

}


