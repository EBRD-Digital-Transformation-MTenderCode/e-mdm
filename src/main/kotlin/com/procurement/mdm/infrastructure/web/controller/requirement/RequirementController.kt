package com.procurement.mdm.infrastructure.web.controller.requirement

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.procurement.mdm.application.service.requirement.RequirementService
import com.procurement.mdm.infrastructure.exception.CountryRequestParameterMissingException
import com.procurement.mdm.infrastructure.exception.LanguageRequestParameterMissingException
import com.procurement.mdm.infrastructure.exception.PhaseRequestParameterMissingException
import com.procurement.mdm.infrastructure.exception.PmdRequestParameterMissingException
import com.procurement.mdm.infrastructure.exception.RequirementGroupIdParameterMissingException
import com.procurement.mdm.infrastructure.web.dto.ApiResponse
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/requirements")
class RequirementController(private val requirementService: RequirementService) {

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun getAllRequirementGroups(
        @RequestParam(value = "lang", required = false) lang: String?,
        @RequestParam(value = "country", required = false) country: String?,
        @RequestParam(value = "pmd", required = false) pmd: String?,
        @RequestParam(value = "phase", required = false) phase: String?,
        @RequestParam(value = "requirementGroupId", required = false) requirementGroupId: String?
    ): RequirementsApiResponse {

        if (lang == null)
            throw LanguageRequestParameterMissingException()
        if (country == null)
            throw CountryRequestParameterMissingException()
        if (pmd == null)
            throw PmdRequestParameterMissingException()
        if (phase == null)
            throw PhaseRequestParameterMissingException()
        if (requirementGroupId == null)
            throw RequirementGroupIdParameterMissingException()

        return requirementService.getAll(
            country = country,
            language = lang,
            pmd = pmd,
            phase = phase,
            requirementGroup = requirementGroupId
        )
            .map { Requirement(id = it.id, description = it.description, title = it.title) }
            .let { RequirementsApiResponse(it) }
    }

    class RequirementsApiResponse(requirements: List<Requirement>) :
        ApiResponse<List<Requirement>>(requirements)

    data class Requirement(
        @field:JsonProperty("id") @param:JsonProperty("id") val id: String,
        @field:JsonProperty("title") @param:JsonProperty("title") val title: String,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        @field:JsonProperty("description") @param:JsonProperty("description") val description: String?
    )
}
