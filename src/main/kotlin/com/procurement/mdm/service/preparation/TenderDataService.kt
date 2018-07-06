package com.procurement.mdm.service.preparation

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ObjectNode
import com.procurement.mdm.exception.ErrorException
import com.procurement.mdm.exception.ErrorType
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
        validationService.checkLanguage(cm.context.language)
        val entity = cpvRepository.findByCpvKeyCodeAndCpvKeyLanguageCode(
                code = getCpvCode(cm),
                languageCode = cm.context.language)
                ?: throw ErrorException(ErrorType.CPV_CODE_UNKNOWN)
        val response = setCpvData(cm, entity)
        return getResponseDto(data = response, id = cm.id)
    }

    fun getCpvCode(cm: CommandMessage): String {
        val data = getData(cm)
        return try {
            data.get("classification").get("id").asText()
        } catch (e: Exception) {
            throw ErrorException(ErrorType.INVALID_DATA, cm.id)
        }
    }

    fun setCpvData(cm: CommandMessage, entity: Cpv): JsonNode? {
        val data = getData(cm)
        return try {
            data.put("mainProcurementCategory", entity.mainProcurementCategory)
            val classificationNode = data.get("classification") as ObjectNode
            classificationNode.put("scheme", "cpv")
            classificationNode.put("description", entity.name)
            data
        } catch (e: Exception) {
            throw ErrorException(ErrorType.INVALID_DATA, cm.id)
        }
    }

    fun getData(cm: CommandMessage): ObjectNode {
        return (cm.data as ObjectNode?) ?: throw ErrorException(ErrorType.INVALID_DATA, cm.id)
    }
}

