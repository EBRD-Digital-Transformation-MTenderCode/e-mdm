package com.procurement.mdm.service.preparation

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ObjectNode
import com.procurement.mdm.exception.ErrorType
import com.procurement.mdm.exception.ExErrorException
import com.procurement.mdm.exception.InErrorException
import com.procurement.mdm.model.dto.CommandMessage
import com.procurement.mdm.model.dto.ResponseDto
import com.procurement.mdm.model.dto.getResponseDto
import com.procurement.mdm.model.entity.Cpv
import com.procurement.mdm.repository.CpvRepository
import com.procurement.mdm.service.ValidationService
import org.springframework.stereotype.Service

interface TenderDataService {

    fun tenderCPV(cm: CommandMessage): ResponseDto
}

@Service
class TenderDataServiceImpl(private val validationService: ValidationService,
                            private val cpvRepository: CpvRepository) : TenderDataService {

    override fun tenderCPV(cm: CommandMessage): ResponseDto {
        validationService.checkLanguage(languageCode = cm.context.language, internal = true)
        val entity = cpvRepository.findByCpvKeyCodeAndCpvKeyLanguageCode(
                code = getCpvCode(cm),
                languageCode = cm.context.language)
                ?: throw InErrorException(ErrorType.CPV_CODE_UNKNOWN)
        val response = setCpvData(cm, entity)
        return getResponseDto(data = response, id = cm.id)
    }

    fun getCpvCode(cm: CommandMessage): String {
        val data = getData(cm)
        return data.get("classification").get("id").asText()
    }

    fun setCpvData(cm: CommandMessage, entity: Cpv): JsonNode? {
        val data = getData(cm)
        data.put("mainProcurementCategory", entity.mainProcurementCategory)
        val classificationNode = data.get("classification") as ObjectNode
        classificationNode.put("scheme", "CPV")
        classificationNode.put("description", entity.name)
        return data
    }

    fun getData(cm: CommandMessage): ObjectNode {
        return (cm.data as ObjectNode?) ?: throw InErrorException(ErrorType.INVALID_DATA, cm.id)
    }
}

