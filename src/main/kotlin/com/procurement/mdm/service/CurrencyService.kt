package com.procurement.mdm.service

import com.procurement.mdm.model.dto.ResponseDto
import com.procurement.mdm.model.dto.getResponseDto
import com.procurement.mdm.model.entity.getItems
import com.procurement.mdm.repository.CurrencyRepository
import org.springframework.stereotype.Service

interface CurrencyService {

    fun getCurrencies(languageCode: String, countryCode: String): ResponseDto

}

@Service
class CurrencyServiceImpl(private val currencyRepository: CurrencyRepository,
                          private val validationService: ValidationService) : CurrencyService {

    override fun getCurrencies(languageCode: String, countryCode: String): ResponseDto {
        validationService.checkLanguage(languageCode = languageCode)
        val country = validationService.getCountry(
                languageCode = languageCode,
                countryCode = countryCode)
        val entities = currencyRepository.findByCurrencyKeyLanguageCodeAndCountries(languageCode, country)
        val defaultValue = entities.asSequence().firstOrNull { it.default }?.currencyKey?.code
        return getResponseDto(
                default = defaultValue,
                items = entities.getItems())
    }
}
