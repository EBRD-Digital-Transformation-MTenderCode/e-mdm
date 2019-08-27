package com.procurement.mdm.infrastructure.web.controller.organization

import com.fasterxml.jackson.annotation.JsonProperty
import com.procurement.mdm.application.service.organization.OrganizationSchemeService
import com.procurement.mdm.infrastructure.exception.CountryRequestParameterMissingException
import com.procurement.mdm.infrastructure.web.dto.ApiResponse
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping
class OrganizationSchemeController(private val organizationSchemeService: OrganizationSchemeService) {

    @GetMapping("/organization/schemes")
    @ResponseStatus(HttpStatus.OK)
    fun getCountryById(
        @RequestParam(value = "country", required = false) country: String?
    ): OrganizationSchemesApiResponse {

        if (country == null)
            throw CountryRequestParameterMissingException()

        val schemesCodes = organizationSchemeService.findAllOnlyCode(country = country)
        return OrganizationSchemesApiResponse(
            OrganizationSchemes(
                schemes = schemesCodes
            )
        )
    }

    class OrganizationSchemesApiResponse(schemes: OrganizationSchemes) :
        ApiResponse<OrganizationSchemes>(schemes)

    data class OrganizationSchemes(
        @field:JsonProperty("schemes") @param:JsonProperty("schemes") val schemes: List<String>
    )
}
