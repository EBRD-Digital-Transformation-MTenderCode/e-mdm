package com.procurement.mdm.infrastructure.web.controller.unit

import com.fasterxml.jackson.annotation.JsonProperty
import com.procurement.mdm.application.service.unit.UnitsService
import com.procurement.mdm.infrastructure.exception.LanguageRequestParameterMissingException
import com.procurement.mdm.infrastructure.web.dto.ApiResponse
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/units")
class UnitsController(private val unitsService: UnitsService) {

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun getClassificationById(
        @RequestParam(value = "lang", required = false) lang: String?,
        @PathVariable(value = "id") id: String
    ): ClassificationApiResponse {
        if (lang == null)
            throw LanguageRequestParameterMissingException()

        return unitsService.getById(id = id, language = lang)
            .let { unit -> Unit(id = unit.id, name = unit.name) }
            .let { ClassificationApiResponse(it) }
    }

    class ClassificationApiResponse(unit: Unit) : ApiResponse<Unit>(unit)

    data class Unit(
        @field:JsonProperty("id") @param:JsonProperty("id") val id: String,
        @field:JsonProperty("name") @param:JsonProperty("name") val name: String
    )
}
