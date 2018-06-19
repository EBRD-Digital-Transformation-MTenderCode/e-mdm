package com.procurement.mdm.controller

import com.procurement.budget.model.bpe.ResponseDto
import com.procurement.mdm.service.CPVService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/cpv")
class CPVController(private val cpvService: CPVService) {

    @GetMapping
    fun getCpv(@RequestParam lang: String,
               @RequestParam(required = false) code: String?): ResponseEntity<ResponseDto> {
        return ResponseEntity(cpvService.getCPV(lang, code), HttpStatus.OK)
    }
}
