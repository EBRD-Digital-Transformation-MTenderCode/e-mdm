package com.procurement.mdm.service

import com.procurement.mdm.model.bpe.ResponseDto
import com.procurement.mdm.repository.CurrencyRepository
import org.springframework.stereotype.Service

interface CurrencyService {

    fun getCurrencies(lang: String, country: String): ResponseDto

}

@Service
class CurrencyServiceImpl(private val currencyRepository: CurrencyRepository) : CurrencyService {

    override fun getCurrencies(lang: String, country: String): ResponseDto {
        val currencies = currencyRepository.findByLanguageCodeAndCountriesCode(lang, country)
        val defaultValue = currencies.asSequence().firstOrNull { it.default }?.code
        return ResponseDto(default = defaultValue, data = currencies)
    }
}
