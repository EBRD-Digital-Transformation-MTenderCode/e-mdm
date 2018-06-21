package com.procurement.mdm.controller

import com.procurement.mdm.model.dto.ResponseDto
import com.procurement.mdm.service.CountryService
import com.procurement.mdm.service.ValidationService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@CrossOrigin(maxAge = 3600)
@RequestMapping("/country")
class CountryController(private val validationService: ValidationService,
                        private val countryService: CountryService) {

    @GetMapping
    fun getCountries(@RequestParam lang: String,
                     @RequestParam(required = false) internal: Boolean = false): ResponseEntity<ResponseDto> {
        validationService.lang(lang.toUpperCase(), internal)
        return ResponseEntity(
                countryService.getCountriesByLanguage(lang.toUpperCase(), internal),
                HttpStatus.OK)
    }

}
