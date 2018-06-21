package com.procurement.mdm.controller

import com.procurement.mdm.model.dto.ResponseDto
import com.procurement.mdm.service.ValidationService
import com.procurement.mdm.service.UnitService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@CrossOrigin(maxAge = 3600)
@RequestMapping("/unit")
class UnitController(private val validationService: ValidationService,
                     private val unitService: UnitService) {

    @GetMapping
    fun getUnits(@RequestParam lang: String,
                 @RequestParam unitClass: String,
                 @RequestParam(required = false) internal: Boolean = false): ResponseEntity<ResponseDto> {
        validationService.lang(lang.toUpperCase(), internal)
        validationService.unitClass(unitClass.toUpperCase(), internal)
        return ResponseEntity(
                unitService.getUnit(lang.toUpperCase(), unitClass.toUpperCase(), internal),
                HttpStatus.OK)
    }
}
