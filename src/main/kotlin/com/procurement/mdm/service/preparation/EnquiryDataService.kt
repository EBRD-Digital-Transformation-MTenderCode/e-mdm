package com.procurement.mdm.service.preparation

import com.procurement.access.utils.toObject
import com.procurement.mdm.exception.ErrorType
import com.procurement.mdm.exception.InErrorException
import com.procurement.mdm.model.dto.CommandMessage
import com.procurement.mdm.model.dto.ResponseDto
import com.procurement.mdm.model.dto.data.BidData
import com.procurement.mdm.model.dto.data.EnquiryData
import com.procurement.mdm.model.dto.getResponseDto
import com.procurement.mdm.service.ValidationService
import org.springframework.stereotype.Service

interface EnquiryDataService {

    fun processEnquiryData(cm: CommandMessage): ResponseDto
}

@Service
class EnquiryDataServiceImpl(private val validationService: ValidationService,
                             private val organizationDataService: OrganizationDataService
) : EnquiryDataService {

    override fun processEnquiryData(cm: CommandMessage): ResponseDto {
        val lang = cm.context.language
        val country = validationService.getCountry(languageCode = lang, countryCode = cm.context.country)
        val dto = getData(cm)
        val author = dto.author
        organizationDataService.processOrganization(author, country)
        return getResponseDto(data = dto, id = cm.id)
    }

    private fun getData(cm: CommandMessage): EnquiryData {
        if (cm.data.size() == 0) throw InErrorException(ErrorType.INVALID_DATA, null, cm.id)
        return toObject(EnquiryData::class.java, cm.data)
    }

}

