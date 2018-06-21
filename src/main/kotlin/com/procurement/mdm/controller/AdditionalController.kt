package com.procurement.mdm.controller

import com.procurement.mdm.model.bpe.ResponseDto
import com.procurement.mdm.service.AdditionalService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@CrossOrigin(maxAge = 3600)
@RequestMapping("/additional")
class AdditionalController(private val additionalService: AdditionalService) {

    @GetMapping("/holidays")
    fun getHolidays(@RequestParam lang: String,
                    @RequestParam country: String): ResponseEntity<ResponseDto> {
        return ResponseEntity(
                additionalService.getHolidays(lang.toUpperCase(), country.toUpperCase()),
                HttpStatus.OK)
    }

    @GetMapping("/bank")
    fun getBank(@RequestParam lang: String,
                @RequestParam country: String): ResponseEntity<ResponseDto> {
        return ResponseEntity(
                additionalService.getBank(lang.toUpperCase(), country.toUpperCase()),
                HttpStatus.OK)
    }

    @GetMapping("/gpaAnnexes")
    fun getGPAnnexes(@RequestParam lang: String,
                     @RequestParam country: String): ResponseEntity<ResponseDto> {
        return ResponseEntity(
                additionalService.getGPAnnexes(lang.toUpperCase(), country.toUpperCase()),
                HttpStatus.OK)
    }
}
