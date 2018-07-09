package com.procurement.mdm.controller

import com.procurement.mdm.model.dto.ResponseDto
import com.procurement.mdm.service.UnitClassService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@CrossOrigin(maxAge = 3600)
@RequestMapping("/unit-class")
class UnitClassController(private val unitClassService: UnitClassService) {

    @GetMapping
    fun getUnitClasses(@RequestParam lang: String): ResponseEntity<ResponseDto> {
        return ResponseEntity(
                unitClassService.getUnitClassesByLanguage(
                        languageCode = lang.toLowerCase()),
                HttpStatus.OK)
    }
}
