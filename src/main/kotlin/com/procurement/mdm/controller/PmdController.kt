package com.procurement.mdm.controller

import com.procurement.mdm.model.dto.ResponseDto
import com.procurement.mdm.service.PmdService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@CrossOrigin(maxAge = 3600)
@RequestMapping("/pmd")
class PmdController(private val pmdService: PmdService) {

    @GetMapping
    fun getPmd(@RequestParam lang: String,
               @RequestParam country: String): ResponseEntity<ResponseDto> {
        return ResponseEntity(
                pmdService.getPmd(
                        languageCode = lang.toLowerCase(),
                        countryCode = country.toUpperCase()),
                HttpStatus.OK)
    }
}
