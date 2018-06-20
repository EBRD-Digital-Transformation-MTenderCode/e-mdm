package com.procurement.mdm.controller

import com.procurement.mdm.model.dto.ResponseDto
import com.procurement.mdm.service.AdditionalService
import com.procurement.mdm.service.MainService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/additional")
class AdditionalController(private val mainService: MainService,
        private val additionalService: AdditionalService) {

    @GetMapping("/holidays")
    fun getHolidays(@RequestParam lang: String,
                    @RequestParam country: String,
                    @RequestParam(required = false) internal: Boolean = false): ResponseEntity<ResponseDto> {

        mainService.validateParams(lang.toUpperCase(), country.toUpperCase(), internal)
        return ResponseEntity(
                additionalService.getHolidays(lang.toUpperCase(), country.toUpperCase(), internal),
                HttpStatus.OK)
    }

    @GetMapping("/bank")
    fun getBank(@RequestParam lang: String,
                @RequestParam country: String,
                @RequestParam(required = false) internal: Boolean = false): ResponseEntity<ResponseDto> {
        mainService.validateParams(lang.toUpperCase(), country.toUpperCase(), internal)
        return ResponseEntity(
                additionalService.getBank(lang.toUpperCase(), country.toUpperCase(), internal),
                HttpStatus.OK)
    }

    @GetMapping("/gpaAnnexes")
    fun getGPAnnexes(@RequestParam lang: String,
                     @RequestParam country: String,
                     @RequestParam(required = false) internal: Boolean = false): ResponseEntity<ResponseDto> {
        mainService.validateParams(lang.toUpperCase(), country.toUpperCase(), internal)
        return ResponseEntity(
                additionalService.getGPAnnexes(lang.toUpperCase(), country.toUpperCase(), internal),
                HttpStatus.OK)
    }
}
