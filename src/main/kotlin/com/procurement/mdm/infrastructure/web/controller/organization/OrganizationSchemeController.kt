package com.procurement.mdm.infrastructure.web.controller.organization

import com.fasterxml.jackson.annotation.JsonProperty
import com.procurement.mdm.application.service.organization.OrganizationSchemeService
import com.procurement.mdm.infrastructure.exception.CountryRequestParameterMissingException
import com.procurement.mdm.infrastructure.exception.RequestPayloadMissingException
import com.procurement.mdm.infrastructure.web.dto.ApiResponse
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping
class OrganizationSchemeController(private val organizationSchemeService: OrganizationSchemeService) {

    @GetMapping("/organization/schemes")
    @ResponseStatus(HttpStatus.OK)
    fun getSchemesByCountry(
        @RequestParam(value = "country", required = false) country: String?
    ): OrganizationSchemesApiResponse {

        if (country == null)
            throw CountryRequestParameterMissingException()

        val schemesCodes = organizationSchemeService.find(country = country)
        return OrganizationSchemesApiResponse(
            OrganizationSchemesApiResponse.OrganizationSchemes(
                schemes = schemesCodes
            )
        )
    }

    @PostMapping("/organization/schemes")
    @ResponseStatus(HttpStatus.OK)
    fun getSchemesByCountries(
        @RequestBody(required = false) payload: OrganizationSchemesApiRequest?
    ): OrganizationSchemesByCountriesApiResponse {

        if (payload == null)
            throw RequestPayloadMissingException()

        val schemesCodes = organizationSchemeService.find(countries = payload.countries)
        return OrganizationSchemesByCountriesApiResponse(
            OrganizationSchemesByCountriesApiResponse.Elements(
                elements = schemesCodes.map { (country, schemes) ->
                    OrganizationSchemesByCountriesApiResponse.Elements.Element(
                        country = country.value,
                        schemes = schemes
                    )
                }
            )
        )
    }

    class OrganizationSchemesApiRequest(
        @field:JsonProperty("countries") @param:JsonProperty("countries") val countries: List<String>
    )

    class OrganizationSchemesApiResponse(schemes: OrganizationSchemes) :
        ApiResponse<OrganizationSchemesApiResponse.OrganizationSchemes>(schemes) {

        data class OrganizationSchemes(
            @field:JsonProperty("schemes") @param:JsonProperty("schemes") val schemes: List<String>
        )
    }

    class OrganizationSchemesByCountriesApiResponse(elements: Elements) :
        ApiResponse<OrganizationSchemesByCountriesApiResponse.Elements>(elements) {

        data class Elements(
            @field:JsonProperty("elements") @param:JsonProperty("elements") val elements: List<Element>
        ) {
            data class Element(
                @field:JsonProperty("country") @param:JsonProperty("country") val country: String,
                @field:JsonProperty("schemes") @param:JsonProperty("schemes") val schemes: List<String>
            )
        }
    }
}
