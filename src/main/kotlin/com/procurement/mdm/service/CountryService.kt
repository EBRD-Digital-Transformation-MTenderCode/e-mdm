package com.procurement.mdm.service

import com.procurement.budget.model.bpe.ResponseDto
import com.procurement.mdm.repository.CountryRepository
import org.springframework.stereotype.Service

interface CountryService {

    fun getAllCountries(): ResponseDto

    fun getCountriesByLanguage(lang: String): ResponseDto

}

@Service
class CountryServiceImpl(private val countryRepository: CountryRepository) : CountryService {

    override fun getAllCountries(): ResponseDto {
        return ResponseDto(default = null, data = countryRepository.findAll())
    }

    override fun getCountriesByLanguage(lang: String): ResponseDto {
        val countries = countryRepository.findCountriesByLanguageCode(lang)
        val defaultValue = countries.asSequence().firstOrNull{ it.default }?.code
        return ResponseDto(default = defaultValue, data = countries)
    }
}
