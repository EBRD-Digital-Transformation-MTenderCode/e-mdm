package com.procurement.mdm.infrastructure.web.controller.requirement.group

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.procurement.mdm.application.service.requirement.group.RequirementGroupService
import com.procurement.mdm.infrastructure.exception.CountryRequestParameterMissingException
import com.procurement.mdm.infrastructure.exception.CriterionRequestParameterMissingException
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
@RequestMapping("/requirementGroups")
class RequirementGroupController(private val requirementGroupService: RequirementGroupService) {

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun getAllRequirementGroups(
        @RequestParam(value = "lang", required = false) lang: String?,
        @RequestParam(value = "country", required = false) country: String?,
        @RequestParam(value = "pmd", required = false) pmd: String?,
        @RequestParam(value = "phase", required = false) phase: String?,
        @RequestParam(value = "criterionId", required = false) criterionId: String?
    ): RequirementGroupsApiResponse {

        if (lang == null)
            throw LanguageRequestParameterMissingException()
        if (country == null)
            throw CountryRequestParameterMissingException()
        if (pmd == null)
            throw PmdRequestParameterMissingException()
        if (phase == null)
            throw PhaseRequestParameterMissingException()
        if (criterionId == null)
            throw CriterionRequestParameterMissingException()

        return requirementGroupService.getAll(
            country = country,
            language = lang,
            pmd = pmd,
            phase = phase,
            criterion = criterionId
        )
            .map { RequirementGroup(id = it.id, description = it.description) }
            .let { RequirementGroupsApiResponse(it) }
    }

    class RequirementGroupsApiResponse(requirementGroups: List<RequirementGroup>) :
        ApiResponse<List<RequirementGroup>>(requirementGroups)

    data class RequirementGroup(
        @field:JsonProperty("id") @param:JsonProperty("id") val id: String,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        @field:JsonProperty("description") @param:JsonProperty("description") val description: String?
    )
}
