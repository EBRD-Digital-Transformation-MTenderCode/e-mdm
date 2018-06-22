package com.procurement.mdm.service

import com.procurement.mdm.model.dto.ResponseDto
import com.procurement.mdm.model.dto.getResponseDto
import com.procurement.mdm.model.entity.getItems
import com.procurement.mdm.repository.CpvsRepository
import org.springframework.stereotype.Service

interface CpvsService {

    fun getCpvs(languageCode: String, parentCode: String?, internal: Boolean): ResponseDto
}

@Service
class CpvsServiceImpl(private val cpvsRepository: CpvsRepository,
                     private val validationService: ValidationService) : CpvsService {

    override fun getCpvs(languageCode: String, parentCode: String?, internal: Boolean): ResponseDto {
        validationService.checkLanguage(languageCode, internal)
        val entities = when (parentCode) {
            null -> cpvsRepository.findByParentAndCpvsKeyLanguageCode(languageCode = languageCode)
            else -> {
                validationService.checkCpvsParent(parentCode = parentCode, languageCode = languageCode, internal = internal)
                cpvsRepository.findByParentAndCpvsKeyLanguageCode(parentCode = parentCode, languageCode = languageCode)
            }
        }
        return getResponseDto(
                default = null,
                items = entities.getItems(),
                internal = internal)
    }
}

