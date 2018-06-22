package com.procurement.mdm.service

import com.procurement.mdm.model.dto.ResponseDto
import com.procurement.mdm.model.dto.getResponseDto
import com.procurement.mdm.repository.CPVsRepository
import org.springframework.stereotype.Service

interface CPVsService {

    fun getCPVs(languageCode: String, parentCode: String?, internal: Boolean): ResponseDto
}

@Service
class CPVsServiceImpl(private val cpvsRepository: CPVsRepository,
                      private val validationService: ValidationService) : CPVsService {

    override fun getCPVs(languageCode: String, parentCode: String?, internal: Boolean): ResponseDto {
        val languageId = validationService.getLanguageId(languageCode, internal)
        val entities = when (parentCode) {
            null -> cpvsRepository.findByLanguageIdAndParent(languageId)
            else -> {
                val parentId = validationService.getCpvsParentId(languageId, parentCode, internal)
                cpvsRepository.findByLanguageIdAndParent(languageId, parentId)
            }
        }
        return getResponseDto(
                default = null,
                items = entities,
                internal = internal)
    }
}
