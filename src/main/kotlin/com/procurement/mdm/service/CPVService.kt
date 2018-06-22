package com.procurement.mdm.service

import com.procurement.mdm.model.dto.ResponseDto
import com.procurement.mdm.model.dto.getResponseDto
import com.procurement.mdm.model.entity.getItems
import com.procurement.mdm.repository.CPVRepository
import org.springframework.stereotype.Service

interface CPVService {

    fun getCPV(languageCode: String, parentCode: String?, internal: Boolean): ResponseDto
}

@Service
class CPVServiceImpl(private val cpvRepository: CPVRepository,
                     private val validationService: ValidationService) : CPVService {

    override fun getCPV(languageCode: String, parentCode: String?, internal: Boolean): ResponseDto {
        validationService.checkLanguage(languageCode, internal)
        val entities = when (parentCode) {
            null -> cpvRepository.findByParentAndCpvKeyLanguageCode(languageCode = languageCode)
            else -> {
                validationService.checkCpvParent(parentCode = parentCode, languageCode = languageCode, internal = internal)
                cpvRepository.findByParentAndCpvKeyLanguageCode(parentCode = parentCode, languageCode = languageCode)
            }
        }
        return getResponseDto(
                default = null,
                items = entities.getItems(),
                internal = internal)
    }
}

