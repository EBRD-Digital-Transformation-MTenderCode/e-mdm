package com.procurement.mdm.infrastructure.web.controller.address

import com.fasterxml.jackson.annotation.JsonProperty
import com.procurement.mdm.application.service.address.AddressCountryService
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
@RequestMapping("/addresses/countries")
class AddressCountryController(private val addressCountryService: AddressCountryService) {

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun getAllCountries(@RequestParam(value = "language", required = false) language: String?): CountriesApiResponse {

        if (language == null)
            throw LanguageRequestParameterMissingException()

        return addressCountryService.getAll(language = language)
            .map {
                Country(
                    id = it.id,
                    description = it.description,
                    scheme = it.scheme,
                    uri = it.uri
                )
            }
            .let { CountriesApiResponse(it) }
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun getCountryById(
        @PathVariable(value = "id") id: String,
        @RequestParam(value = "language", required = false) language: String?
    ): CountryApiResponse {

        if (language == null)
            throw LanguageRequestParameterMissingException()

        val countryIdentifier = addressCountryService.getBy(country = id, language = language)

        return CountryApiResponse(
            country = Country(
                id = countryIdentifier.id,
                description = countryIdentifier.description,
                scheme = countryIdentifier.scheme,
                uri = countryIdentifier.uri
            )
        )
    }

    class CountryApiResponse(country: Country) : ApiResponse<Country>(country)

    class CountriesApiResponse(countries: List<Country>) : ApiResponse<List<Country>>(countries)

    data class Country(
        @field:JsonProperty("scheme") @param:JsonProperty("scheme") val scheme: String,
        @field:JsonProperty("id") @param:JsonProperty("id") val id: String,
        @field:JsonProperty("description") @param:JsonProperty("description") val description: String,
        @field:JsonProperty("uri") @param:JsonProperty("uri") val uri: String
    )
}
