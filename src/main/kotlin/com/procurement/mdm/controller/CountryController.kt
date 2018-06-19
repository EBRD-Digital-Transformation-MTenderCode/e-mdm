package com.procurement.mdm.controller

import com.procurement.mdm.model.bpe.ResponseDto
import com.procurement.mdm.service.CountryService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/country")
class CountryController(private val countryService: CountryService) {

    @GetMapping
    fun getCountries(@RequestParam lang: String): ResponseEntity<ResponseDto> {
        return ResponseEntity(
                countryService.getCountriesByLanguage(lang.toUpperCase()),
                HttpStatus.OK)
    }

}
