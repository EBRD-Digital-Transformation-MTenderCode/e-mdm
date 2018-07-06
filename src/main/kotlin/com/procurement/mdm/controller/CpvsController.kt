package com.procurement.mdm.controller

import com.procurement.mdm.model.dto.ResponseDto
import com.procurement.mdm.service.CpvsService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@CrossOrigin(maxAge = 3600)
@RequestMapping("/cpvs")
class CpvsController(private val cpvsService: CpvsService) {

    @GetMapping
    fun getCpvs(@RequestParam lang: String,
                @RequestParam(required = false) internal: Boolean = false): ResponseEntity<ResponseDto> {
        return ResponseEntity(
                cpvsService.getCpvs(
                        languageCode = lang.toLowerCase(),
                        internal = internal),
                HttpStatus.OK)
    }
}
