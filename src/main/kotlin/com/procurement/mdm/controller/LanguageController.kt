package com.procurement.mdm.controller

import com.procurement.mdm.model.dto.ResponseDto
import com.procurement.mdm.service.LanguageService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@CrossOrigin(maxAge = 3600)
@RequestMapping("/language")
class LanguageController(private val languageService: LanguageService) {

    @GetMapping
    fun getLanguages(): ResponseEntity<ResponseDto> {
        return ResponseEntity(
                languageService.getLanguages(),
                HttpStatus.OK)
    }
}
