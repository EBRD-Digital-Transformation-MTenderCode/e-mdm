package com.procurement.mdm.controller

import com.procurement.mdm.model.dto.ResponseDto
import com.procurement.mdm.service.CPVService
import com.procurement.mdm.service.ValidationService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@CrossOrigin(maxAge = 3600)
@RequestMapping("/cpv")
class CPVController(private val validationService: ValidationService,
                    private val cpvService: CPVService) {

    @GetMapping
    fun getCpv(@RequestParam lang: String = "",
               @RequestParam(required = false) code: String?,
               @RequestParam(required = false) internal: Boolean = false): ResponseEntity<ResponseDto> {
        validationService.lang(lang.toUpperCase(), internal)
        return ResponseEntity(
                cpvService.getCPV(lang.toUpperCase(), code?.toUpperCase(), internal),
                HttpStatus.OK)
    }
}
