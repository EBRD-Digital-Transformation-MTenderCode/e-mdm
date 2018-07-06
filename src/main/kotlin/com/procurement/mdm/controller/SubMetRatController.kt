package com.procurement.mdm.controller

import com.procurement.mdm.model.dto.ResponseDto
import com.procurement.mdm.service.PmdService
import com.procurement.mdm.service.SubMetDetService
import com.procurement.mdm.service.SubMetRatService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@CrossOrigin(maxAge = 3600)
@RequestMapping("/smr")
class SubMetRatController(private val subMetRatService: SubMetRatService) {

    @GetMapping
    fun getSmd(@RequestParam lang: String): ResponseEntity<ResponseDto> {
        return ResponseEntity(
                subMetRatService.getSubMetRat(
                        languageCode = lang.toLowerCase()),
                HttpStatus.OK)
    }
}
