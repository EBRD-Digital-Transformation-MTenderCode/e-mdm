package com.procurement.mdm.service

import com.procurement.mdm.model.dto.ResponseDto
import com.procurement.mdm.model.dto.getResponseDto
import com.procurement.mdm.repository.CountryRepository
import org.springframework.stereotype.Service

interface CountryService {

    fun getAllCountries(internal: Boolean): ResponseDto

    fun getCountriesByLanguage(lang: String, internal: Boolean): ResponseDto

}

@Service
class CountryServiceImpl(private val countryRepository: CountryRepository) : CountryService {

    override fun getAllCountries(internal: Boolean): ResponseDto {
        return getResponseDto(
                default = null,
                items = countryRepository.findAll(),
                internal = internal)
    }

    override fun getCountriesByLanguage(lang: String, internal: Boolean): ResponseDto {
        val entities = countryRepository.findByLanguageCode(lang)
        val defaultValue = entities.asSequence().firstOrNull { it.default }?.code
        return getResponseDto(
                default = defaultValue,
                items = entities,
                internal = internal)
    }
}
