package com.procurement.mdm.controller

import com.procurement.mdm.model.dto.ResponseDto
import com.procurement.mdm.service.PmdService
import com.procurement.mdm.service.SubMetDetService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@CrossOrigin(maxAge = 3600)
@RequestMapping("/smd")
class TranslateController(private val subMetDetService: SubMetDetService) {

    @GetMapping
    fun getSmd(@RequestParam lang: String,
                @RequestParam(required = false) internal: Boolean = false): ResponseEntity<ResponseDto> {
        return ResponseEntity(
                subMetDetService.getSubMetDet(
                        languageCode = lang.toLowerCase(),
                        internal = internal),
                HttpStatus.OK)
    }
}
