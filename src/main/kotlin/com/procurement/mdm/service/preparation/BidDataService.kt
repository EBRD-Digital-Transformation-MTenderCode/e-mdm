package com.procurement.mdm.service.preparation

import com.procurement.access.utils.toObject
import com.procurement.mdm.exception.ErrorType
import com.procurement.mdm.exception.InErrorException
import com.procurement.mdm.model.dto.CommandMessage
import com.procurement.mdm.model.dto.ResponseDto
import com.procurement.mdm.model.dto.data.BidData
import com.procurement.mdm.model.dto.getResponseDto
import com.procurement.mdm.service.ValidationService
import org.springframework.stereotype.Service

interface BidDataService {

    fun createBid(cm: CommandMessage): ResponseDto
}

@Service
class BidDataServiceImpl(private val validationService: ValidationService,
                         private val organizationDataService: OrganizationDataService
) : BidDataService {

    override fun createBid(cm: CommandMessage): ResponseDto {
        val lang = cm.context.language
        val country = validationService.getCountry(languageCode = lang, countryCode = cm.context.country)
        val dto = getData(cm)
        val tenderers = dto.tenderers
        tenderers.forEach { tenderer ->
            organizationDataService.processOrganization(tenderer, country)
        }
        return getResponseDto(data = dto, id = cm.id)
    }

    private fun getData(cm: CommandMessage): BidData {
        cm.data ?: throw InErrorException(ErrorType.INVALID_DATA, cm.id)
        return toObject(BidData::class.java, cm.data)
    }

}

