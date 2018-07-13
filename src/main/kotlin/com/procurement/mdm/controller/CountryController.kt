package com.procurement.mdm.controller

import com.procurement.mdm.model.dto.ResponseDto
import com.procurement.mdm.service.CountryService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URLDecoder

@RestController
@CrossOrigin(maxAge = 3600)
@RequestMapping("/country")
class CountryController(private val countryService: CountryService) {

    @GetMapping
    fun getCountriesByLanguage(@RequestParam lang: String): ResponseEntity<ResponseDto> {
        return ResponseEntity(
                countryService.getCountries(languageCode = lang.toLowerCase()),
                HttpStatus.OK)
    }

    @GetMapping("/{codeOrName}")
    fun getCountries(@RequestParam(value = "lang", required = false) lang: String?,
                     @PathVariable("codeOrName") codeOrName: String?): ResponseEntity<ResponseDto> {
        return ResponseEntity(
                countryService.getCountries(
                        languageCode = lang?.toLowerCase(),
                        codeOrName =  URLDecoder.decode(codeOrName, "UTF-8").toUpperCase()),
                HttpStatus.OK)
    }
}
