package com.procurement.mdm.service

import com.procurement.budget.model.bpe.ResponseDto
import com.procurement.mdm.repository.DocumentTypeRepository
import org.springframework.stereotype.Service

interface DocumentTypeService {

    fun getDocumentType(lang: String, entityKind: String): ResponseDto
}

@Service
class DocumentTypeServiceImpl(private val documentTypeRepository: DocumentTypeRepository) : DocumentTypeService {

    override fun getDocumentType(lang: String, entityKind: String): ResponseDto {
        return ResponseDto(null,
                documentTypeRepository.findByLanguageCodeAndEntityKindsCode(lang, entityKind))
    }
}
