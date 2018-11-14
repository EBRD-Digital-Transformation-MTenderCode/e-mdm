package com.procurement.mdm.service.preparation

import com.procurement.access.utils.toObject
import com.procurement.mdm.exception.ErrorType
import com.procurement.mdm.exception.InErrorException
import com.procurement.mdm.model.dto.CommandMessage
import com.procurement.mdm.model.dto.ResponseDto
import com.procurement.mdm.model.dto.data.CD
import com.procurement.mdm.model.dto.getResponseDto
import com.procurement.mdm.service.ValidationService
import org.springframework.stereotype.Service

interface ContractDataService {

    fun processContractData(cm: CommandMessage): ResponseDto
}

@Service
class ContractDataServiceImpl(private val validationService: ValidationService,
                              private val addressDataService: AddressDataService) : ContractDataService {

    override fun processContractData(cm: CommandMessage): ResponseDto {
        val lang = cm.context.language
        validationService.getLanguage(languageCode = lang, internal = true)
        val country = validationService.getCountry(languageCode = lang, countryCode = cm.context.country)
        val dto = getData(cm)
        val items = dto.items
        for (item in items) {
            addressDataService.processAddress(item.deliveryAddress, country)
        }
        return getResponseDto(data = dto, id = cm.id)
    }

    private fun getData(cm: CommandMessage): CD {
        if (cm.data.size() == 0) throw InErrorException(ErrorType.INVALID_DATA, null, cm.id)
        return toObject(CD::class.java, cm.data)
    }

}