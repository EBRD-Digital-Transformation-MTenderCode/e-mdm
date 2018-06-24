package com.procurement.mdm.controller

import com.procurement.mdm.model.dto.ResponseDto
import com.procurement.mdm.service.UnitService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@CrossOrigin(maxAge = 3600)
@RequestMapping("/unit")
class UnitController(private val unitService: UnitService) {

    @GetMapping
    fun getUnits(@RequestParam lang: String,
                 @RequestParam unitClass: String,
                 @RequestParam(required = false) internal: Boolean = false): ResponseEntity<ResponseDto> {
        return ResponseEntity(
                unitService.getUnit(
                        languageCode = lang.toLowerCase(),
                        unitClassCode = unitClass.toUpperCase(),
                        internal = internal),
                HttpStatus.OK)
    }
}
