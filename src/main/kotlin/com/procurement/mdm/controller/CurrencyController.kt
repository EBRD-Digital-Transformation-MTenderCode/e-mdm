package com.procurement.mdm.controller

import com.procurement.mdm.model.dto.ResponseDto
import com.procurement.mdm.service.CurrencyService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@CrossOrigin(maxAge = 3600)
@RequestMapping("/currency")
class CurrencyController(private val currencyService: CurrencyService) {

    @GetMapping
    fun getCurrencies(@RequestParam lang: String,
                      @RequestParam country: String,
                      @RequestParam(required = false) internal: Boolean = false): ResponseEntity<ResponseDto> {
        return ResponseEntity(
                currencyService.getCurrencies(
                        languageCode = lang.toUpperCase(),
                        countryCode = country.toUpperCase(),
                        internal = internal),
                HttpStatus.OK)
    }
}
