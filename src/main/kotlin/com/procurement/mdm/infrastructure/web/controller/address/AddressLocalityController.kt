package com.procurement.mdm.infrastructure.web.controller.address

import com.fasterxml.jackson.annotation.JsonProperty
import com.procurement.mdm.application.service.address.AddressLocalityService
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
@RequestMapping("/addresses/countries/{countryId}/regions/{regionId}/localities")
class AddressLocalityController(private val addressLocalityService: AddressLocalityService) {

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    fun getAll(
        @PathVariable(value = "countryId") countryId: String,
        @PathVariable(value = "regionId") regionId: String,
        @RequestParam(value = "lang", required = false) lang: String?
    ): LocalitiesApiResponse {

        if (lang == null)
            throw LanguageRequestParameterMissingException()

        val localities = addressLocalityService.getBy(
            country = countryId,
            region = regionId,
            language = lang
        ).map { localityIdentifier ->
            Locality(
                id = localityIdentifier.id,
                description = localityIdentifier.description,
                scheme = localityIdentifier.scheme,
                uri = localityIdentifier.uri
            )
        }

        return LocalitiesApiResponse(localities = localities)
    }

    @GetMapping("/{localityId}")
    @ResponseStatus(HttpStatus.OK)
    fun getById(
        @PathVariable(value = "countryId") countryId: String,
        @PathVariable(value = "regionId") regionId: String,
        @PathVariable(value = "localityId") localityId: String,
        @RequestParam(value = "lang", required = false) lang: String?,
        @RequestParam(value = "scheme", required = false) scheme: String?
    ): LocalityApiResponse {

        if (lang == null)
            throw LanguageRequestParameterMissingException()

        val localityIdentifier = addressLocalityService.getBy(
            locality = localityId,
            country = countryId,
            region = regionId,
            language = lang,
            scheme = scheme
        )

        return LocalityApiResponse(
            locality = Locality(
                id = localityIdentifier.id,
                description = localityIdentifier.description,
                scheme = localityIdentifier.scheme,
                uri = localityIdentifier.uri
            )
        )
    }

    @GetMapping("/attributes/schemes")
    @ResponseStatus(HttpStatus.OK)
    fun getSchemes(
        @PathVariable(value = "countryId") countryId: String,
        @PathVariable(value = "regionId") regionId: String
    ): SchemesApiResponse {
        val schemes = addressLocalityService.getAllSchemes(country = countryId, region = regionId)
        return SchemesApiResponse(schemes = Schemes(schemes))
    }

    class LocalityApiResponse(locality: Locality) : ApiResponse<Locality>(locality)

    class LocalitiesApiResponse(localities: List<Locality>) : ApiResponse<List<Locality>>(localities)

    class SchemesApiResponse(schemes: Schemes) : ApiResponse<Schemes>(schemes)

    data class Schemes(
        @field:JsonProperty("schemes") @param:JsonProperty("schemes") val scheme: List<String>
    )

    data class Locality(
        @field:JsonProperty("scheme") @param:JsonProperty("scheme") val scheme: String,
        @field:JsonProperty("id") @param:JsonProperty("id") val id: String,
        @field:JsonProperty("description") @param:JsonProperty("description") val description: String,
        @field:JsonProperty("uri") @param:JsonProperty("uri") val uri: String
    )
}
