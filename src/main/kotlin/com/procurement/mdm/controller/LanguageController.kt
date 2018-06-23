package com.procurement.mdm.controller

import com.procurement.mdm.model.dto.ResponseDto
import com.procurement.mdm.service.LanguageService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@CrossOrigin(maxAge = 3600)
@RequestMapping("/language")
class LanguageController(private val languageService: LanguageService) {

    @GetMapping
    fun getLanguages(@RequestParam(required = false) internal: Boolean = false): ResponseEntity<ResponseDto> {
        return ResponseEntity(
                languageService.getLanguages(internal),
                HttpStatus.OK)
    }
}
