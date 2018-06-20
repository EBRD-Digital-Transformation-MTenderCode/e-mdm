package com.procurement.mdm.service

import com.procurement.mdm.model.dto.ResponseDto
import com.procurement.mdm.model.dto.getResponseDto
import com.procurement.mdm.repository.CurrencyRepository
import org.springframework.stereotype.Service

interface CurrencyService {

    fun getCurrencies(lang: String, country: String, internal: Boolean): ResponseDto

}

@Service
class CurrencyServiceImpl(private val currencyRepository: CurrencyRepository) : CurrencyService {

    override fun getCurrencies(lang: String, country: String, internal: Boolean): ResponseDto {
        val entities = currencyRepository.findByLanguageCodeAndCountriesCode(lang, country)
        val defaultValue = entities.asSequence().firstOrNull { it.default }?.code
        return getResponseDto(
                default = defaultValue,
                items = entities,
                internal = internal)
    }
}
