package com.procurement.mdm.service

import com.procurement.mdm.model.dto.ResponseDto
import com.procurement.mdm.model.dto.getResponseDto
import com.procurement.mdm.model.entity.getItems
import com.procurement.mdm.repository.TranslateRepository
import org.springframework.stereotype.Service

interface TranslateService {

    fun getTranslate(languageCode: String,  code: String,  internal: Boolean): ResponseDto
}

@Service
class TranslateServiceImpl(private val subMetDetRepository: TranslateRepository,
                           private val validationService: ValidationService) : TranslateService {

    override fun getTranslate(languageCode: String, internal: Boolean): ResponseDto {
        validationService.checkLanguage(languageCode, internal)
        val entities = subMetDetRepository.findByTranslateKeyLanguageCode(languageCode = languageCode)
        return getResponseDto(
            default = null,
            items = entities.getItems(),
            internal = internal)
    }
}

