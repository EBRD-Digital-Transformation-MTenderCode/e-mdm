package com.procurement.mdm.controller

import com.procurement.mdm.model.entity.Country
import com.procurement.mdm.service.CountryService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/countries")
class CountryController(private val countryService: CountryService) {

    @GetMapping
    fun getCountries(): ResponseEntity<List<Country>> {
        val countries = countryService.getAllCountries()
        return ResponseEntity(countries, HttpStatus.OK)
    }

    @GetMapping("/byCode")
    fun getCountriesByCode(@RequestParam code: String): ResponseEntity<List<Country>> {
        val countries = countryService.getCountriesByCode(code)
        return ResponseEntity(countries, HttpStatus.OK)
    }

    @GetMapping("/byName")
    fun getCountriesByName(@RequestParam name: String): ResponseEntity<List<Country>> {
        val countries = countryService.getCountriesByName(name)
        return ResponseEntity(countries, HttpStatus.OK)
    }

}
