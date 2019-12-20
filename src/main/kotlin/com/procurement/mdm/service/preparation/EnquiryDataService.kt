package com.procurement.mdm.service.preparation

import com.procurement.mdm.exception.ErrorType
import com.procurement.mdm.exception.InErrorException
import com.procurement.mdm.model.dto.CommandMessage
import com.procurement.mdm.model.dto.ResponseDto
import com.procurement.mdm.model.dto.data.EnquiryData
import com.procurement.mdm.model.dto.getResponseDto
import com.procurement.mdm.service.ValidationService
import com.procurement.mdm.utils.toObject
import org.springframework.stereotype.Service

interface EnquiryDataService {

    fun processEnquiryData(cm: CommandMessage): ResponseDto
}

@Service
class EnquiryDataServiceImpl(
    private val validationService: ValidationService,
    private val organizationDataService: OrganizationDataService
) : EnquiryDataService {

    override fun processEnquiryData(cm: CommandMessage): ResponseDto {
        val lang = cm.context.language
        val dto = getData(cm)
        val author = dto.author
        val countryCode = author.address!!.addressDetails.country.id
        val country = validationService.getCountry(languageCode = lang, countryCode = countryCode)
        organizationDataService.processOrganization(author, country)
        return getResponseDto(data = dto, id = cm.id)
    }

    private fun getData(cm: CommandMessage): EnquiryData =
        if (cm.data.size() != 0)
            toObject(EnquiryData::class.java, cm.data)
        else
            throw InErrorException(ErrorType.INVALID_DATA, null, cm.id)
}
