package com.procurement.mdm.service

import com.procurement.mdm.model.dto.ResponseDto
import com.procurement.mdm.model.dto.getResponseDto
import com.procurement.mdm.model.entity.getItems
import com.procurement.mdm.repository.CountryRepository
import org.springframework.stereotype.Service

interface CountryService {

    fun getCountriesByLanguage(languageCode: String): ResponseDto

}

@Service
class CountryServiceImpl(private val countryRepository: CountryRepository,
                         private val validationService: ValidationService) : CountryService {

    override fun getCountriesByLanguage(languageCode: String): ResponseDto {
        validationService.checkLanguage(languageCode)
        val entities = countryRepository.findByCountryKeyLanguageCode(languageCode)
        val defaultValue = entities.asSequence().firstOrNull { it.default }?.countryKey?.code
        return getResponseDto(default = defaultValue, items = entities.getItems())
    }
}
