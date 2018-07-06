package com.procurement.mdm.service

import com.procurement.mdm.exception.ErrorType
import com.procurement.mdm.exception.ErrorException
import com.procurement.mdm.model.entity.Country
import com.procurement.mdm.model.entity.UnitClass
import com.procurement.mdm.repository.*
import org.springframework.stereotype.Service

interface ValidationService {

    fun checkLanguage(languageCode: String)

    fun getCountry(languageCode: String, countryCode: String): Country

    fun getUnitClass(languageCode: String, code: String): UnitClass

    fun checkEntityKind(code: String)

    fun checkCpvParent(parentCode: String, languageCode: String)
}

@Service
class ValidationServiceImpl(private val languageRepository: LanguageRepository,
                            private val countryRepository: CountryRepository,
                            private val unitClassRepository: UnitClassRepository,
                            private val entityKindRepository: EntityKindRepository,
                            private val cpvRepository: CpvRepository
) : ValidationService {

    override fun checkLanguage(languageCode: String) {
        languageRepository.findByCode(code = languageCode)
                ?: throw ErrorException(ErrorType.LANG_UNKNOWN)
    }

    override fun getCountry(languageCode: String, countryCode: String): Country {
        languageRepository.findByCode(code = languageCode)
                ?: throw ErrorException(ErrorType.LANG_UNKNOWN)
        return countryRepository.findByCountryKeyCodeAndCountryKeyLanguageCode(code = countryCode, languageCode = languageCode)
                ?: throw ErrorException(ErrorType.COUNTRY_UNKNOWN)
    }

    override fun getUnitClass(languageCode: String, code: String): UnitClass {
        languageRepository.findByCode(code = languageCode)
                ?: throw ErrorException(ErrorType.LANG_UNKNOWN)
        return unitClassRepository.findByUnitClassKeyCodeAndUnitClassKeyLanguageCode(code = code, languageCode = languageCode)
                ?: throw ErrorException(ErrorType.UNIT_CLASS_UNKNOWN)
    }

    override fun checkEntityKind(code: String) {
        entityKindRepository.findByCode(code)
                ?: throw ErrorException(ErrorType.ENTITY_KIND_UNKNOWN)
    }

    override fun checkCpvParent(parentCode: String, languageCode: String) {
        cpvRepository.findByCpvKeyCodeAndCpvKeyLanguageCode(code = parentCode, languageCode = languageCode)
                ?: throw ErrorException(ErrorType.CPV_CODE_UNKNOWN)
    }
}


