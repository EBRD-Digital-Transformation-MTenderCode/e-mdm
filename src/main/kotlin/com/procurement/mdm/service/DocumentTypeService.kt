package com.procurement.mdm.service

import com.procurement.mdm.model.dto.ResponseDto
import com.procurement.mdm.model.dto.getResponseDto
import com.procurement.mdm.repository.DocumentTypeRepository
import org.springframework.stereotype.Service

interface DocumentTypeService {

    fun getDocumentType(lang: String, entityKind: String, internal: Boolean): ResponseDto
}

@Service
class DocumentTypeServiceImpl(private val documentTypeRepository: DocumentTypeRepository) : DocumentTypeService {

    override fun getDocumentType(lang: String, entityKind: String, internal: Boolean): ResponseDto {
        return getResponseDto(
                default = null,
                items = documentTypeRepository.findByLanguageCodeAndEntityKindsCode(lang, entityKind),
                internal = internal)
    }
}
