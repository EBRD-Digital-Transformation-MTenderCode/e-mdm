package com.procurement.mdm.service

import com.procurement.mdm.model.dto.ResponseDto
import com.procurement.mdm.model.dto.getResponseDto
import com.procurement.mdm.repository.CPVRepository
import org.springframework.stereotype.Service

interface CPVService {

    fun getCPV(lang: String, code: String?, internal: Boolean): ResponseDto
}

@Service
class CPVServiceImpl(private val cpvRepository: CPVRepository) : CPVService {

    override fun getCPV(lang: String, code: String?, internal: Boolean): ResponseDto {
        val entities = when (code) {
            null -> cpvRepository.findByLanguageCodeAndParent(lang)
            else -> cpvRepository.findByLanguageCodeAndParent(lang, code)
        }
        return getResponseDto(
                default = null,
                items = entities,
                internal = internal)
    }
}
