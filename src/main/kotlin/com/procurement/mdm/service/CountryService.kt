package com.procurement.mdm.service

import com.procurement.mdm.exception.ErrorType
import com.procurement.mdm.exception.ExErrorException
import com.procurement.mdm.model.dto.ResponseDto
import com.procurement.mdm.model.dto.getResponseDto
import com.procurement.mdm.model.entity.getItems
import com.procurement.mdm.repository.CountryRepository
import org.springframework.stereotype.Service

interface CountryService {

    fun getCountries(languageCode: String, codeOrName: String? = null): ResponseDto

}

@Service
class CountryServiceImpl(private val countryRepository: CountryRepository,
                         private val validationService: ValidationService) : CountryService {

    override fun getCountries(languageCode: String, codeOrName: String?): ResponseDto {
        validationService.checkLanguage(languageCode)
        return if (codeOrName != null) {
            val entity = countryRepository.findByCountryKeyLanguageCodeAndCountryKeyCode(languageCode = languageCode, code = codeOrName)
                    ?: countryRepository.findByCountryKeyLanguageCodeAndName(languageCode = languageCode, name = codeOrName)
                    ?: throw ExErrorException(ErrorType.COUNTRY_UNKNOWN)
            getResponseDto(items = listOf(entity).getItems())
        } else {
            val entities = countryRepository.findByCountryKeyLanguageCode(languageCode)
            val defaultValue = entities.asSequence().firstOrNull { it.default }?.countryKey?.code
            getResponseDto(default = defaultValue, items = entities.getItems())
        }

    }
}
