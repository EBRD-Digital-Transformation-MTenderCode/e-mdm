package com.procurement.mdm.controller

import com.procurement.budget.model.bpe.ResponseDto
import com.procurement.mdm.service.RegistrationSchemeService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/registrationScheme")
class RegistrationSchemeController(private val registrationSchemeService: RegistrationSchemeService) {

    @GetMapping
    fun getRegistrationSchemes(@RequestParam lang: String,
                               @RequestParam country: String): ResponseEntity<ResponseDto> {
        return ResponseEntity(
                registrationSchemeService.getRegistrationScheme(lang.toUpperCase(), country.toUpperCase()),
                HttpStatus.OK)
    }
}
