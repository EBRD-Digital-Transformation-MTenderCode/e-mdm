package com.procurement.mdm.infrastructure.web.controller.criterion

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.procurement.mdm.application.service.criterion.CriterionService
import com.procurement.mdm.infrastructure.exception.CountryRequestParameterMissingException
import com.procurement.mdm.infrastructure.exception.LanguageRequestParameterMissingException
import com.procurement.mdm.infrastructure.exception.PhaseRequestParameterMissingException
import com.procurement.mdm.infrastructure.exception.PmdRequestParameterMissingException
import com.procurement.mdm.infrastructure.web.dto.ApiResponse
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/criteria")
class CriterionController(private val criterionService: CriterionService) {

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun getCriteriaById(
        @RequestParam(value = "lang", required = false) lang: String?,
        @RequestParam(value = "country", required = false) country: String?,
        @RequestParam(value = "pmd", required = false) pmd: String?,
        @RequestParam(value = "phase", required = false) phase: String?
    ): CriteriaApiResponse {
        if (lang == null)
            throw LanguageRequestParameterMissingException()
        if (country == null)
            throw CountryRequestParameterMissingException()
        if (pmd == null)
            throw PmdRequestParameterMissingException()
        if (phase == null)
            throw PhaseRequestParameterMissingException()

        return criterionService.getAll(country = country, language = lang, pmd = pmd, phase = phase)
            .map { criterion ->
                Criterion(
                    id = criterion.id,
                    title = criterion.title,
                    description = criterion.description
                )
            }.let { CriteriaApiResponse(it) }
    }

    class CriteriaApiResponse(criteria: List<Criterion>) : ApiResponse<List<Criterion>>(criteria)

    data class Criterion(
        @field:JsonProperty("id") @param:JsonProperty("id") val id: String,
        @field:JsonProperty("title") @param:JsonProperty("title") val title: String,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        @field:JsonProperty("description") @param:JsonProperty("description") val description: String?
    )
}
