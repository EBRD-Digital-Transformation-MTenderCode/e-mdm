package com.procurement.mdm.controller

import com.procurement.mdm.model.dto.ResponseDto
import com.procurement.mdm.service.ValidationService
import com.procurement.mdm.service.RegionService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@CrossOrigin(maxAge = 3600)
@RequestMapping("/region")
class RegionController(private val validationService: ValidationService,
                       private val regionService: RegionService) {

    @GetMapping
    fun getRegions(@RequestParam lang: String,
                   @RequestParam country: String,
                   @RequestParam(required = false) internal: Boolean = false): ResponseEntity<ResponseDto> {
        validationService.country(lang.toUpperCase(), country.toUpperCase(), internal)
        return ResponseEntity(
                regionService.getRegion(lang.toUpperCase(), country.toUpperCase(), internal),
                HttpStatus.OK)
    }
}
