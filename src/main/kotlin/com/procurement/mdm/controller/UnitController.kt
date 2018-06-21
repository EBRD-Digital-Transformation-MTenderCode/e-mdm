package com.procurement.mdm.controller

import com.procurement.mdm.model.bpe.ResponseDto
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
                 @RequestParam unitClass: String): ResponseEntity<ResponseDto> {
        return ResponseEntity(
                unitService.getUnit(lang.toUpperCase(), unitClass.toUpperCase()),
                HttpStatus.OK)
    }
}
