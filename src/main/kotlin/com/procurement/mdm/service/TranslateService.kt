package com.procurement.mdm.service

import com.procurement.mdm.exception.ErrorType
import com.procurement.mdm.exception.ExErrorException
import com.procurement.mdm.model.dto.ResponseDto
import com.procurement.mdm.model.dto.getResponseDto
import com.procurement.mdm.model.entity.getItems
import com.procurement.mdm.repository.TranslateRepository
import org.springframework.stereotype.Service

interface TranslateService {

    fun getTranslate(languageCode: String, code: String): ResponseDto
}

@Service
class TranslateServiceImpl(private val subMetDetRepository: TranslateRepository,
                           private val validationService: ValidationService) : TranslateService {

    override fun getTranslate(languageCode: String, code: String): ResponseDto {
        validationService.getLanguage(languageCode)
        val entity = subMetDetRepository.findByTranslateKeyCodeAndTranslateKeyLanguageCode(code = code, languageCode = languageCode)
                ?: throw ExErrorException(ErrorType.TRANSLATION_UNKNOWN)
        return getResponseDto(items = listOf(entity).getItems())
    }
}

