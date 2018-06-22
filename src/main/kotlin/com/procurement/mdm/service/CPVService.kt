package com.procurement.mdm.service

import com.procurement.mdm.model.dto.ResponseDto
import com.procurement.mdm.model.dto.getResponseDto
import com.procurement.mdm.repository.CPVRepository
import org.springframework.stereotype.Service

interface CPVService {

    fun getCPV(languageCode: String, parentCode: String?, internal: Boolean): ResponseDto
}

@Service
class CPVServiceImpl(private val cpvRepository: CPVRepository,
                     private val validationService: ValidationService) : CPVService {

    override fun getCPV(languageCode: String, parentCode: String?, internal: Boolean): ResponseDto {
        val languageId = validationService.getLanguageId(languageCode, internal)
//        val entities = when (parentCode) {
//            null -> cpvRepository.findByLanguageIdAndParent(languageId)
//            else -> {
//                val parentId = validationService.getCpvParentId(languageId, parentCode, internal)
//                cpvRepository.findByLanguageIdAndParent(languageId, parentId)
//            }
//        }

        val entities = when (parentCode) {
            null -> cpvRepository.findByParentAndCpvIdentityLanguageId(languageId = languageId)
            else -> {
                cpvRepository.findByParentAndCpvIdentityLanguageId(parentCode = parentCode, languageId = languageId)
            }
        }
        return getResponseDto(
                default = null,
                items = entities,
                internal = internal)
    }
}
