package com.procurement.mdm.service

import com.procurement.mdm.model.dto.ResponseDto
import com.procurement.mdm.model.dto.getResponseDto
import com.procurement.mdm.repository.CPVsRepository
import org.springframework.stereotype.Service

interface CPVsService {

    fun getCPVs(lang: String, code: String?, internal: Boolean): ResponseDto
}

@Service
class CPVsServiceImpl(private val cpvsRepository: CPVsRepository) : CPVsService {

    override fun getCPVs(lang: String, code: String?, internal: Boolean): ResponseDto {
        val entities = when (code) {
            null -> cpvsRepository.findByLanguageCodeAndParent(lang)
            else -> cpvsRepository.findByLanguageCodeAndParent(lang, code)
        }
        return getResponseDto(
                default = null,
                items = entities,
                internal = internal)
    }
}
