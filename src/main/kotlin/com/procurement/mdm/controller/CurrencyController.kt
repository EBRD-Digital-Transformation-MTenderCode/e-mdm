package com.procurement.mdm.controller

import com.procurement.mdm.model.dto.ResponseDto
import com.procurement.mdm.service.CurrencyService
import com.procurement.mdm.service.ValidationService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@CrossOrigin(maxAge = 3600)
@RequestMapping("/currency")
class CurrencyController(private val validationService: ValidationService,
                         private val currencyService: CurrencyService) {

    @GetMapping
    fun getCurrencies(@RequestParam lang: String,
                      @RequestParam country: String,
                      @RequestParam(required = false) internal: Boolean = false): ResponseEntity<ResponseDto> {
        validationService.country(lang.toUpperCase(), country.toUpperCase(), internal)
        return ResponseEntity(
                currencyService.getCurrencies(lang.toUpperCase(), country.toUpperCase(), internal),
                HttpStatus.OK)
    }
}
