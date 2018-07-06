package com.procurement.mdm.controller

import com.procurement.mdm.model.dto.ResponseDto
import com.procurement.mdm.service.CpvService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@CrossOrigin(maxAge = 3600)
@RequestMapping("/cpv")
class CpvController(private val cpvService: CpvService) {

    @GetMapping
    fun getCpv(@RequestParam lang: String,
               @RequestParam(required = false) code: String?): ResponseEntity<ResponseDto> {
        return ResponseEntity(
                cpvService.getCpv(
                        languageCode = lang.toLowerCase(),
                        parentCode = code?.toUpperCase()),
                HttpStatus.OK)
    }
}
