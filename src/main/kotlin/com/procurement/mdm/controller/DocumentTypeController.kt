package com.procurement.mdm.controller

import com.procurement.mdm.model.bpe.ResponseDto
import com.procurement.mdm.service.DocumentTypeService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/documentType")
class DocumentTypeController(private val documentTypeService: DocumentTypeService) {

    @GetMapping
    fun getDocumentTypes(@RequestParam lang: String,
                         @RequestParam entityKind: String): ResponseEntity<ResponseDto> {
        return ResponseEntity(
                documentTypeService.getDocumentType(lang.toUpperCase(), entityKind.toUpperCase()),
                HttpStatus.OK)
    }
}
