package com.procurement.mdm.controller

import com.procurement.mdm.model.dto.ResponseDto
import com.procurement.mdm.service.RegionService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@CrossOrigin(maxAge = 3600)
@RequestMapping("/region")
class RegionController(private val regionService: RegionService) {

    @GetMapping
    fun getRegions(@RequestParam lang: String,
                   @RequestParam country: String): ResponseEntity<ResponseDto> {
        return ResponseEntity(
                regionService.getRegion(
                        languageCode = lang.toLowerCase(),
                        countryCode = country.toUpperCase()),
                HttpStatus.OK)
    }
}
