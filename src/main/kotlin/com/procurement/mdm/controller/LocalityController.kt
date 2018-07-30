package com.procurement.mdm.controller

import com.procurement.mdm.model.dto.ResponseDto
import com.procurement.mdm.service.LocalityService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@CrossOrigin(maxAge = 3600)
@RequestMapping("/locality")
class LocalityController(private val localityService: LocalityService) {

    @GetMapping
    fun getRegions(@RequestParam lang: String,
                   @RequestParam country: String,
                   @RequestParam region: String): ResponseEntity<ResponseDto> {
        return ResponseEntity(
                localityService.getLocality(
                        languageCode = lang.toLowerCase(),
                        countryCode = country.toUpperCase(),
                        regionCode = region.toUpperCase()),
                HttpStatus.OK)
    }
}
