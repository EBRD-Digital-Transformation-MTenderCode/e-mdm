package com.procurement.mdm.infrastructure.web.controller.organization

import com.fasterxml.jackson.annotation.JsonProperty
import com.procurement.mdm.application.service.organization.OrganizationScaleService
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
class OrganizationScaleController(private val organizationScaleService: OrganizationScaleService) {

    @GetMapping("/organization/scales")
    @ResponseStatus(HttpStatus.OK)
    fun getCountryById(
        @RequestParam(value = "country", required = false) country: String?
    ): OrganizationScaleApiResponse {

        if (country == null)
            throw CountryRequestParameterMissingException()

        val scalesCodes = organizationScaleService.findAllOnlyCode(country = country)
        return OrganizationScaleApiResponse(
            OrganizationScale(
                scalesCodes
            )
        )
    }

    class OrganizationScaleApiResponse(scales: OrganizationScale) :
        ApiResponse<OrganizationScale>(scales)

    data class OrganizationScale(
        @field:JsonProperty("scales") @param:JsonProperty("scales") val scales: List<String>
    )
}
