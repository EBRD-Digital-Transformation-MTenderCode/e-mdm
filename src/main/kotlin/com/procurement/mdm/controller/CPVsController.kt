package com.procurement.mdm.controller

import com.procurement.mdm.model.bpe.ResponseDto
import com.procurement.mdm.service.CPVsService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@CrossOrigin(maxAge = 3600)
@RequestMapping("/cpvs")
class CPVsController(private val cpvsService: CPVsService) {

    @GetMapping
    fun getCpvs(@RequestParam lang: String,
                @RequestParam(required = false) code: String?): ResponseEntity<ResponseDto> {
        return ResponseEntity(
                cpvsService.getCPVs(lang.toUpperCase(), code?.toUpperCase()),
                HttpStatus.OK)
    }
}
