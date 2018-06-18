package com.procurement.mdm.controller

import com.procurement.mdm.model.entity.Cpv
import com.procurement.mdm.service.CpvService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/cpv")
class CpvController(private val cpvService: CpvService) {

    @GetMapping("/byLanguage")
    fun getCpvByLanguageCode(@RequestParam("languageCode") languageCode: String): ResponseEntity<List<Cpv>> {
        val cpv = cpvService.getCpvByLanguageCode(languageCode)
        return ResponseEntity(cpv, HttpStatus.OK)
    }

    @GetMapping("/byParam")
    fun getCpvByParam(@RequestParam("languageCode") languageCode: String,
                      @RequestParam(value = "group", required = false) group: Int?,
                      @RequestParam(value = "parent", required = false) parent: String): ResponseEntity<List<Cpv>> {

        val cpv = cpvService.getCpvByParam(languageCode, group, parent)
        return ResponseEntity(cpv, HttpStatus.OK)
    }
}
