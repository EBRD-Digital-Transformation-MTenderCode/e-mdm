package com.procurement.mdm.service

import com.procurement.mdm.model.bpe.ResponseDto
import com.procurement.mdm.repository.CPVRepository
import org.springframework.stereotype.Service

interface CPVService {

    fun getCPV(lang: String, code: String?): ResponseDto
}

@Service
class CPVServiceImpl(private val cpvRepository: CPVRepository) : CPVService {

    override fun getCPV(lang: String, code: String?): ResponseDto {
        return if (code != null) {
            ResponseDto(null, cpvRepository.findByLanguageCodeAndParent(lang, code))
        } else {
            ResponseDto(null, cpvRepository.findByLanguageCodeAndParent(lang))
        }
    }
}
