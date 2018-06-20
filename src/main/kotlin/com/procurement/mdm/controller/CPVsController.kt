package com.procurement.mdm.controller

import com.procurement.mdm.model.dto.ResponseDto
import com.procurement.mdm.service.CPVsService
import com.procurement.mdm.service.MainService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/cpvs")
class CPVsController(private val mainService: MainService,
                     private val cpvsService: CPVsService) {

    @GetMapping
    fun getCpvs(@RequestParam lang: String,
                @RequestParam(required = false) code: String?,
                @RequestParam(required = false) internal: Boolean = false): ResponseEntity<ResponseDto> {
        mainService.validateParams(lang.toUpperCase(), internal)
        return ResponseEntity(
                cpvsService.getCPVs(lang.toUpperCase(), code?.toUpperCase(), internal),
                HttpStatus.OK)
    }
}
