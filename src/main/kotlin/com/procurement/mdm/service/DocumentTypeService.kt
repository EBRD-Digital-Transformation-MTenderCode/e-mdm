package com.procurement.mdm.service

import com.procurement.mdm.model.dto.ResponseDto
import com.procurement.mdm.model.dto.getResponseDto
import com.procurement.mdm.model.entity.getItems
import com.procurement.mdm.repository.DocumentTypeRepository
import org.springframework.stereotype.Service

interface DocumentTypeService {

    fun getDocumentType(languageCode: String, entityKindCode: String, internal: Boolean): ResponseDto
}

@Service
class DocumentTypeServiceImpl(private val documentTypeRepository: DocumentTypeRepository,
                              private val validationService: ValidationService) : DocumentTypeService {

    override fun getDocumentType(languageCode: String, entityKindCode: String, internal: Boolean): ResponseDto {
        validationService.checkLanguage(languageCode = languageCode, internal = internal)
        validationService.checkEntityKind(code = entityKindCode, internal = internal)
        val entities = documentTypeRepository.findByEntityKindsCodeAndDtKeyLanguageCode(
                entityKindCode = entityKindCode,
                languageCode = languageCode)
        return getResponseDto(
                default = null,
                items = entities.getItems(),
                internal = internal)
    }
}
