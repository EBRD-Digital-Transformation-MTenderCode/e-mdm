package com.procurement.mdm.controller

import com.procurement.mdm.model.dto.ResponseDto
import com.procurement.mdm.service.MainService
import com.procurement.mdm.service.RegionService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@CrossOrigin(maxAge = 3600)
@RequestMapping("/region")
class RegionController(private val mainService: MainService,
                       private val regionService: RegionService) {

    @GetMapping
    fun getRegions(@RequestParam lang: String,
                   @RequestParam country: String,
                   @RequestParam(required = false) internal: Boolean = false): ResponseEntity<ResponseDto> {
        mainService.validateParams(lang.toUpperCase(), country.toUpperCase(), internal)
        return ResponseEntity(
                regionService.getRegion(lang.toUpperCase(), country.toUpperCase(), internal),
                HttpStatus.OK)
    }
}
