package com.procurement.mdm.service

import com.procurement.mdm.model.dto.ResponseDto
import com.procurement.mdm.model.dto.getResponseDto
import com.procurement.mdm.repository.CountryRepository
import org.springframework.stereotype.Service

interface CountryService {

    fun getAllCountries(internal: Boolean): ResponseDto

    fun getCountriesByLanguage(languageCode: String, internal: Boolean): ResponseDto

}

@Service
class CountryServiceImpl(private val countryRepository: CountryRepository,
                         private val validationService: ValidationService) : CountryService {

    override fun getAllCountries(internal: Boolean): ResponseDto {
        return getResponseDto(
                default = null,
                items = countryRepository.findAll(),
                internal = internal)
    }

    override fun getCountriesByLanguage(languageCode: String, internal: Boolean): ResponseDto {
        val languageId = validationService.getLanguageId(languageCode, internal)
        val entities = countryRepository.findByLanguageId(languageId)
        val defaultValue = entities.asSequence().firstOrNull { it.default }?.code
        return getResponseDto(
                default = defaultValue,
                items = entities,
                internal = internal)
    }
}
