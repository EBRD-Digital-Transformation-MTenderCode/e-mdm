//package com.procurement.mdm.service
//
//import com.procurement.mdm.model.dto.ResponseDto
//import com.procurement.mdm.model.dto.getResponseDto
//import com.procurement.mdm.repository.DocumentTypeRepository
//import org.springframework.stereotype.Service
//
//interface DocumentTypeService {
//
//    fun getDocumentType(languageCode: String, entityKindCode: String, internal: Boolean): ResponseDto
//}
//
//@Service
//class DocumentTypeServiceImpl(private val documentTypeRepository: DocumentTypeRepository,
//                              private val validationService: ValidationService) : DocumentTypeService {
//
//    override fun getDocumentType(languageCode: String, entityKindCode: String, internal: Boolean): ResponseDto {
//        val languageId = validationService.checkLanguage(
//                languageCode = languageCode,
//                internal = internal)
//        val entityKindId = validationService.getEntityKindId(
//                code = entityKindCode,
//                internal = internal)
//        return getResponseDto(
//                default = null,
//                items = documentTypeRepository.findByLanguageIdAndEntityKindsId(languageId, entityKindId),
//                internal = internal)
//    }
//}
