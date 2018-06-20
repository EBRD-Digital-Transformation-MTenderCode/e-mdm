package com.procurement.mdm.controller

import com.procurement.mdm.model.dto.ResponseDto
import com.procurement.mdm.service.MainService
import com.procurement.mdm.service.UnitService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/unit")
class UnitController(private val mainService: MainService,
                     private val unitService: UnitService) {

    @GetMapping
    fun getUnits(@RequestParam lang: String,
                 @RequestParam unitClass: String,
                 @RequestParam(required = false) internal: Boolean = false): ResponseEntity<ResponseDto> {
        mainService.validateParams(lang.toUpperCase(), internal)
        return ResponseEntity(
                unitService.getUnit(lang.toUpperCase(), unitClass.toUpperCase(), internal),
                HttpStatus.OK)
    }
}
