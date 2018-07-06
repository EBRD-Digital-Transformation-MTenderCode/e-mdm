package com.procurement.mdm.service

import com.procurement.mdm.model.dto.ResponseDto
import com.procurement.mdm.model.dto.getResponseDto
import com.procurement.mdm.repository.LanguageRepository
import org.springframework.stereotype.Service

interface LanguageService {

    fun getLanguages(): ResponseDto

}

@Service
class LanguageServiceImpl(private val languageRepository: LanguageRepository) : LanguageService {

    override fun getLanguages(): ResponseDto {
        return getResponseDto(items = languageRepository.findAll()
        )
    }
}
