package com.procurement.mdm.service

import com.procurement.mdm.model.dto.ResponseDto
import com.procurement.mdm.model.dto.getResponseDto
import com.procurement.mdm.model.entity.getItems
import com.procurement.mdm.repository.CpvRepository
import org.springframework.stereotype.Service

interface CpvService {

    fun getCpv(languageCode: String, parentCode: String?): ResponseDto
}

@Service
class CpvServiceImpl(private val cpvRepository: CpvRepository,
                     private val validationService: ValidationService) : CpvService {

    override fun getCpv(languageCode: String, parentCode: String?): ResponseDto {
        validationService.checkLanguage(languageCode)
        val entities = when (parentCode) {
            null -> cpvRepository.findByParentAndCpvKeyLanguageCode(languageCode = languageCode)
            else -> {
                validationService.checkCpvParent(parentCode = parentCode, languageCode = languageCode)
                cpvRepository.findByParentAndCpvKeyLanguageCode(parentCode = parentCode, languageCode = languageCode)
            }
        }
        return getResponseDto(items = entities.getItems())
    }
}

