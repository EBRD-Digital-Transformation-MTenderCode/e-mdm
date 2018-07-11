package com.procurement.mdm.service.preparation

import com.fasterxml.jackson.databind.node.ObjectNode
import com.procurement.mdm.exception.ErrorType
import com.procurement.mdm.exception.InErrorException
import com.procurement.mdm.model.dto.CommandMessage
import com.procurement.mdm.model.dto.ResponseDto
import com.procurement.mdm.model.dto.getResponseDto
import com.procurement.mdm.repository.CurrencyRepository
import com.procurement.mdm.service.ValidationService
import org.springframework.stereotype.Service

interface BudgetDataService {

    fun checkCurrency(cm: CommandMessage): ResponseDto
}

@Service
class BudgetDataServiceImpl(private val validationService: ValidationService,
                            private val currencyRepository: CurrencyRepository) : BudgetDataService {

    override fun checkCurrency(cm: CommandMessage): ResponseDto {
        validationService.checkLanguage(languageCode = cm.context.language, internal = true)
        val country = validationService.getCountry(languageCode = cm.context.language, countryCode = cm.context.country)
        val entities = currencyRepository.findByCurrencyKeyLanguageCodeAndCountries(cm.context.language, country)
        val currencyCode = getCurrencyCode(cm)
        entities.asSequence().firstOrNull{it.currencyKey?.code.equals(currencyCode)}
                ?: throw InErrorException(ErrorType.CURRENCY_UNKNOWN)
        return getResponseDto("ok", cm.id)
    }

    fun getCurrencyCode(cm: CommandMessage): String {
        val data = getData(cm)
        return data.get("currency").asText()
    }

    fun getData(cm: CommandMessage): ObjectNode {
        return (cm.data as ObjectNode?) ?: throw InErrorException(ErrorType.INVALID_DATA, cm.id)
    }
}

