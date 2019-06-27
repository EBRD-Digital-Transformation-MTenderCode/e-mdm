package com.procurement.mdm.infrastructure.web.controller.address

import com.fasterxml.jackson.annotation.JsonProperty
import com.procurement.mdm.application.service.address.AddressRegionService
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
@RequestMapping
class AddressRegionController(private val addressRegionService: AddressRegionService) {

    @GetMapping("/addresses/countries/{countryId}/regions/{regionId}")
    @ResponseStatus(HttpStatus.OK)
    fun getCountryById(
        @PathVariable(value = "countryId") countryId: String,
        @PathVariable(value = "regionId") regionId: String,
        @RequestParam(value = "language", required = false) language: String?
    ): RegionApiResponse {

        if (language == null)
            throw LanguageRequestParameterMissingException()

        val regionIdentifier = addressRegionService.getBy(
            region = regionId,
            country = countryId,
            language = language
        )

        return RegionApiResponse(
            region = Region(
                id = regionIdentifier.id,
                description = regionIdentifier.description,
                scheme = regionIdentifier.scheme,
                uri = regionIdentifier.uri
            )
        )
    }

    class RegionApiResponse(region: Region) : ApiResponse<Region>(region)

    data class Region(
        @field:JsonProperty("scheme") @param:JsonProperty("scheme") val scheme: String,
        @field:JsonProperty("id") @param:JsonProperty("id") val id: String,
        @field:JsonProperty("description") @param:JsonProperty("description") val description: String,
        @field:JsonProperty("uri") @param:JsonProperty("uri") val uri: String
    )
}
