package com.procurement.mdm.service

import com.procurement.budget.model.bpe.ResponseDto
import com.procurement.mdm.repository.CPVsRepository
import org.springframework.stereotype.Service

interface CPVsService {

    fun getCPVs(lang: String, code: String?): ResponseDto
}

@Service
class CPVsServiceImpl(private val cpvsRepository: CPVsRepository) : CPVsService {

    override fun getCPVs(lang: String, code: String?): ResponseDto {
        return if (code != null) {
            ResponseDto(null, cpvsRepository.findByLanguageCodeAndParent(lang, code))
        } else {
            ResponseDto(null, cpvsRepository.findByLanguageCodeAndParent(lang))
        }
    }
}
