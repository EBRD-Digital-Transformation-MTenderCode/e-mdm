package com.procurement.mdm.controller

import com.procurement.mdm.model.dto.ResponseDto
import com.procurement.mdm.service.CurrencyService
import com.procurement.mdm.service.MainService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/currency")
class CurrencyController(private val mainService: MainService,
                         private val currencyService: CurrencyService) {

    @GetMapping
    fun getCurrencies(@RequestParam lang: String,
                      @RequestParam country: String,
                      @RequestParam(required = false) internal: Boolean = false): ResponseEntity<ResponseDto> {
        mainService.validateParams(lang.toUpperCase(), country.toUpperCase(), internal)
        return ResponseEntity(
                currencyService.getCurrencies(lang.toUpperCase(), country.toUpperCase(), internal),
                HttpStatus.OK)
    }
}
