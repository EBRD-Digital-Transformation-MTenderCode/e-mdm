package com.procurement.mdm.controller

import com.procurement.mdm.model.dto.ResponseDto
import com.procurement.mdm.service.CPVService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@CrossOrigin(maxAge = 3600)
@RequestMapping("/cpv")
class CPVController(private val cpvService: CPVService) {

    @GetMapping
    fun getCpv(@RequestParam lang: String = "",
               @RequestParam(required = false) code: String?,
               @RequestParam(required = false) internal: Boolean = false): ResponseEntity<ResponseDto> {
        return ResponseEntity(
                cpvService.getCPV(
                        languageCode = lang.toUpperCase(),
                        parentCode = code?.toUpperCase(),
                        internal = internal),
                HttpStatus.OK)
    }
}
