package com.procurement.mdm.controller

import com.procurement.budget.model.bpe.ResponseDto
import com.procurement.mdm.model.entity.Country
import com.procurement.mdm.service.CountryService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/countries")
class CountryController(private val countryService: CountryService) {

    @GetMapping
    fun getCountries(): ResponseEntity<ResponseDto> {
        val countries = countryService.getAllCountries()
        return ResponseEntity(countries, HttpStatus.OK)
    }

    @GetMapping("/byLanguage")
    fun getCountriesByCode(@RequestParam lang: String): ResponseEntity<ResponseDto> {
        return ResponseEntity(countryService.getCountriesByLanguage(lang), HttpStatus.OK)
    }

}
