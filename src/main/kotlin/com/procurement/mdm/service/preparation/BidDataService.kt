package com.procurement.mdm.service.preparation

import com.procurement.mdm.exception.ErrorType
import com.procurement.mdm.exception.InErrorException
import com.procurement.mdm.model.dto.CommandMessage
import com.procurement.mdm.model.dto.ResponseDto
import com.procurement.mdm.model.dto.data.BidData
import com.procurement.mdm.model.dto.getResponseDto
import com.procurement.mdm.service.ValidationService
import com.procurement.mdm.utils.toObject
import org.springframework.stereotype.Service

interface BidDataService {

    fun processBidData(cm: CommandMessage): ResponseDto
}

@Service
class BidDataServiceImpl(
    private val validationService: ValidationService,
    private val organizationDataService: OrganizationDataService
) : BidDataService {

    override fun processBidData(cm: CommandMessage): ResponseDto {
        val lang = cm.context.language
        val dto = getData(cm)
        dto.tenderers
            .forEach { tenderer ->
                val countyCode = tenderer.address!!.addressDetails.country.id
                val country = validationService.getCountry(languageCode = lang, countryCode = countyCode)
                organizationDataService.processOrganization(tenderer, country)
            }
        return getResponseDto(data = dto, id = cm.id)
    }

    private fun getData(cm: CommandMessage): BidData =
        if (cm.data.size() != 0)
            toObject(BidData::class.java, cm.data)
        else
            throw InErrorException(ErrorType.INVALID_DATA, null, cm.id)
}
