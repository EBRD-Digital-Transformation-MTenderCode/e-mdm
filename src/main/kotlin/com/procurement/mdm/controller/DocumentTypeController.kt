package com.procurement.mdm.controller

//import com.procurement.mdm.model.dto.ResponseDto
//import com.procurement.mdm.service.DocumentTypeService
//import org.springframework.http.HttpStatus
//import org.springframework.http.ResponseEntity
//import org.springframework.web.bind.annotation.*
//
//@RestController
//@CrossOrigin(maxAge = 3600)
//@RequestMapping("/document-type")
//class DocumentTypeController(private val documentTypeService: DocumentTypeService) {
//
//    @GetMapping
//    fun getDocumentTypes(@RequestParam lang: String,
//                         @RequestParam entityKind: String,
//                         ): ResponseEntity<ResponseDto> {
//        return ResponseEntity(
//                documentTypeService.getDocumentType(
//                        languageCode = lang.toLowerCase(),
//                        entityKindCode = entityKind.toUpperCase(),
//                        ),
//                HttpStatus.OK)
//    }
//}
