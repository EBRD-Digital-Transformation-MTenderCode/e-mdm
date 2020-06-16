package com.procurement.mdm.infrastructure.web.controller.address

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doThrow
import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.procurement.mdm.application.exception.LocalityNotFoundException
import com.procurement.mdm.application.service.address.AddressLocalityService
import com.procurement.mdm.domain.exception.InvalidCountryCodeException
import com.procurement.mdm.domain.exception.InvalidLanguageCodeException
import com.procurement.mdm.domain.exception.InvalidLocalityCodeException
import com.procurement.mdm.domain.exception.InvalidRegionCodeException
import com.procurement.mdm.domain.exception.LanguageUnknownException
import com.procurement.mdm.domain.model.code.CountryCode
import com.procurement.mdm.domain.model.code.LanguageCode
import com.procurement.mdm.domain.model.code.LocalityCode
import com.procurement.mdm.domain.model.code.RegionCode
import com.procurement.mdm.domain.model.identifier.LocalityIdentifier
import com.procurement.mdm.domain.model.scheme.LocalityScheme
import com.procurement.mdm.infrastructure.web.controller.RestExceptionHandler
import com.procurement.mdm.infrastructure.web.dto.ErrorCode.INTERNAL_SERVER_ERROR
import com.procurement.mdm.infrastructure.web.dto.ErrorCode.INVALID_COUNTRY_CODE
import com.procurement.mdm.infrastructure.web.dto.ErrorCode.INVALID_LANGUAGE_CODE
import com.procurement.mdm.infrastructure.web.dto.ErrorCode.INVALID_LOCALITY_CODE
import com.procurement.mdm.infrastructure.web.dto.ErrorCode.INVALID_REGION_CODE
import com.procurement.mdm.infrastructure.web.dto.ErrorCode.LANGUAGE_REQUEST_PARAMETER_MISSING
import com.procurement.mdm.infrastructure.web.dto.ErrorCode.LANGUAGE_REQUEST_PARAMETER_UNKNOWN
import com.procurement.mdm.infrastructure.web.dto.ErrorCode.LOCALITY_NOT_FOUND
import org.hamcrest.core.IsEqual.equalTo
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders

class AddressLocalityControllerTest {
    companion object {
        private const val COUNTRY = "md"
        private const val EMPTY_COUNTRY = "   "
        private const val INVALID_COUNTRY = "INVALID_COUNTRY"

        private const val REGION = "REGION-1"
        private const val EMPTY_REGION = "   "

        private const val LOCALITY = "LOCALITY-2"
        private const val EMPTY_LOCALITY = "   "

        private const val LANGUAGE = "ro"
        private const val EMPTY_LANGUAGE = "   "
        private const val INVALID_LANGUAGE = "INVALID_LANGUAGE"
        private const val UNKNOWN_LANGUAGE = "ul"

        private const val SCHEME = "scheme-1"
    }

    private lateinit var mockMvc: MockMvc
    private lateinit var addressLocalityService: AddressLocalityService

    @BeforeEach
    fun init() {
        addressLocalityService = mock()

        val controller = AddressLocalityController(addressLocalityService)
        val restExceptionHandler = RestExceptionHandler()
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
            .setControllerAdvice(restExceptionHandler)
            .build()
    }

    @Test
    fun `Getting all localities is successful`() {
        val expected = LocalityIdentifier(
            scheme = "scheme-1",
            id = LOCALITY,
            description = "description-1",
            uri = "https://example-1.com"
        )

        whenever(addressLocalityService.getBy(country = eq(COUNTRY), region = eq(REGION), language = eq(LANGUAGE)))
            .thenReturn(listOf(expected))

        val url = getUrl(country = COUNTRY, region = REGION)
        mockMvc.perform(
            get(url)
                .param("lang", LANGUAGE)
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType("application/json;charset=UTF-8"))
            .andExpect(jsonPath("$.data.length()", equalTo(1)))
            .andExpect(jsonPath("$.data[0].scheme", equalTo(expected.scheme)))
            .andExpect(jsonPath("$.data[0].id", equalTo(expected.id)))
            .andExpect(jsonPath("$.data[0].description", equalTo(expected.description)))
            .andExpect(jsonPath("$.data[0].uri", equalTo(expected.uri)))

        verify(addressLocalityService, times(1))
            .getBy(country = any(), region = any(), language = any())
    }

    @Test
    fun `Getting all localities is successful (list of localities is empty)`() {
        whenever(addressLocalityService.getBy(country = eq(COUNTRY), region = eq(REGION), language = eq(LANGUAGE)))
            .thenReturn(emptyList())

        val url = getUrl(country = COUNTRY, region = REGION)
        mockMvc.perform(
            get(url)
                .param("lang", LANGUAGE)
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType("application/json;charset=UTF-8"))
            .andExpect(jsonPath("$.data.length()", equalTo(0)))

        verify(addressLocalityService, times(1))
            .getBy(country = any(), region = any(), language = any())
    }

    @Test
    fun `Getting all localities is successful (language request parameter is missing)`() {
        val url = getUrl(country = COUNTRY, region = REGION)
        mockMvc.perform(get(url))
            .andExpect(status().isBadRequest)
            .andExpect(content().contentType("application/json;charset=UTF-8"))
            .andExpect(jsonPath("$.errors.length()", equalTo(1)))
            .andExpect(jsonPath("$.errors[0].code", equalTo(LANGUAGE_REQUEST_PARAMETER_MISSING.code)))
            .andExpect(
                jsonPath(
                    "$.errors[0].description",
                    equalTo("The request is missing a required query parameter - 'lang'.")
                )
            )

        verify(addressLocalityService, times(0))
            .getBy(country = any(), region = any(), language = any())
    }

    @Test
    fun `Getting the locality by code is successful`() {
        val expected = LocalityIdentifier(
            scheme = "scheme-1",
            id = LOCALITY,
            description = "description-1",
            uri = "https://example-1.com"
        )

        whenever(
            addressLocalityService.getBy(
                locality = eq(LOCALITY),
                country = eq(COUNTRY),
                region = eq(REGION),
                language = eq(LANGUAGE),
                scheme = eq(SCHEME)
            )
        ).thenReturn(expected)

        val url = getUrl(locality = LOCALITY, country = COUNTRY, region = REGION)
        mockMvc.perform(
            get(url)
                .param("lang", LANGUAGE)
                .param("scheme", SCHEME)
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType("application/json;charset=UTF-8"))
            .andExpect(jsonPath("$.data.scheme", equalTo(expected.scheme)))
            .andExpect(jsonPath("$.data.id", equalTo(expected.id)))
            .andExpect(jsonPath("$.data.description", equalTo(expected.description)))
            .andExpect(jsonPath("$.data.uri", equalTo(expected.uri)))

        verify(addressLocalityService, times(1))
            .getBy(locality = any(), country = any(), region = any(), language = any(), scheme = any())
    }

    @Test
    fun `Getting the locality by code is error (language request parameter is missing)`() {
        val url = getUrl(locality = LOCALITY, country = COUNTRY, region = REGION)
        mockMvc.perform(
            get(url)
        )
            .andExpect(status().isBadRequest)
            .andExpect(content().contentType("application/json;charset=UTF-8"))
            .andExpect(jsonPath("$.errors.length()", equalTo(1)))
            .andExpect(jsonPath("$.errors[0].code", equalTo(LANGUAGE_REQUEST_PARAMETER_MISSING.code)))
            .andExpect(
                jsonPath(
                    "$.errors[0].description",
                    equalTo("The request is missing a required query parameter - 'lang'.")
                )
            )

        verify(addressLocalityService, times(0))
            .getBy(locality = any(), country = any(), region = any(), language = any(), scheme = any())
    }

    @Test
    fun `Getting the locality by code is error (language request parameter is empty)`() {
        doThrow(InvalidLanguageCodeException(description = "Invalid language code (value is blank)."))
            .whenever(addressLocalityService)
            .getBy(
                locality = eq(LOCALITY),
                country = eq(COUNTRY),
                region = eq(REGION),
                language = eq(EMPTY_LANGUAGE),
                scheme = eq(SCHEME)
            )

        val url = getUrl(locality = LOCALITY, country = COUNTRY, region = REGION)
        mockMvc.perform(
            get(url)
                .param("lang", EMPTY_LANGUAGE)
                .param("scheme", SCHEME)
        )
            .andExpect(status().isBadRequest)
            .andExpect(content().contentType("application/json;charset=UTF-8"))
            .andExpect(jsonPath("$.errors.length()", equalTo(1)))
            .andExpect(jsonPath("$.errors[0].code", equalTo(INVALID_LANGUAGE_CODE.code)))
            .andExpect(
                jsonPath(
                    "$.errors[0].description",
                    equalTo("Invalid language code (value is blank).")
                )
            )

        verify(addressLocalityService, times(1))
            .getBy(locality = any(), country = any(), region = any(), language = any(), scheme = any())
    }

    @Test
    fun `Getting the locality by code is error (language request parameter is invalid)`() {

        doThrow(InvalidLanguageCodeException(description = "Invalid language code: '$INVALID_LANGUAGE' (wrong length: '${INVALID_LANGUAGE.length}' required: '2')."))
            .whenever(addressLocalityService)
            .getBy(
                locality = eq(LOCALITY),
                country = eq(COUNTRY),
                region = eq(REGION),
                language = eq(INVALID_LANGUAGE),
                scheme = eq(SCHEME)
            )

        val url = getUrl(locality = LOCALITY, country = COUNTRY, region = REGION)
        mockMvc.perform(
            get(url)
                .param("lang", INVALID_LANGUAGE)
                .param("scheme", SCHEME)
        )
            .andExpect(status().isBadRequest)
            .andExpect(content().contentType("application/json;charset=UTF-8"))
            .andExpect(jsonPath("$.errors.length()", equalTo(1)))
            .andExpect(jsonPath("$.errors[0].code", equalTo(INVALID_LANGUAGE_CODE.code)))
            .andExpect(
                jsonPath(
                    "$.errors[0].description",
                    equalTo("Invalid language code: '$INVALID_LANGUAGE' (wrong length: '${INVALID_LANGUAGE.length}' required: '2').")
                )
            )

        verify(addressLocalityService, times(1))
            .getBy(locality = any(), country = any(), region = any(), language = any(), scheme = any())
    }

    @Test
    fun `Getting the locality by code is error (language request parameter is unknown)`() {
        doThrow(LanguageUnknownException(language = UNKNOWN_LANGUAGE))
            .whenever(addressLocalityService)
            .getBy(
                locality = eq(LOCALITY),
                country = eq(COUNTRY),
                region = eq(REGION),
                language = eq(UNKNOWN_LANGUAGE),
                scheme = eq(SCHEME)
            )

        val url = getUrl(locality = LOCALITY, country = COUNTRY, region = REGION)
        mockMvc.perform(
            get(url)
                .param("lang", UNKNOWN_LANGUAGE)
                .param("scheme", SCHEME)
        )
            .andExpect(status().isBadRequest)
            .andExpect(content().contentType("application/json;charset=UTF-8"))
            .andExpect(jsonPath("$.errors.length()", equalTo(1)))
            .andExpect(jsonPath("$.errors[0].code", equalTo(LANGUAGE_REQUEST_PARAMETER_UNKNOWN.code)))
            .andExpect(
                jsonPath(
                    "$.errors[0].description",
                    equalTo("The unknown code of a language '$UNKNOWN_LANGUAGE'.")
                )
            )

        verify(addressLocalityService, times(1))
            .getBy(locality = any(), country = any(), region = any(), language = any(), scheme = any())
    }

    @Test
    fun `Getting the locality by code is error (country code is empty)`() {
        doThrow(InvalidCountryCodeException(description = "Invalid country code (value is blank)."))
            .whenever(addressLocalityService)
            .getBy(
                locality = eq(LOCALITY),
                country = eq(EMPTY_COUNTRY),
                region = eq(REGION),
                language = eq(LANGUAGE),
                scheme = eq(SCHEME)
            )

        val url = getUrl(locality = LOCALITY, country = EMPTY_COUNTRY, region = REGION)
        mockMvc.perform(
            get(url)
                .param("lang", LANGUAGE)
                .param("scheme", SCHEME)
        )
            .andExpect(status().isBadRequest)
            .andExpect(content().contentType("application/json;charset=UTF-8"))
            .andExpect(jsonPath("$.errors.length()", equalTo(1)))
            .andExpect(jsonPath("$.errors[0].code", equalTo(INVALID_COUNTRY_CODE.code)))
            .andExpect(
                jsonPath(
                    "$.errors[0].description",
                    equalTo("Invalid country code (value is blank).")
                )
            )

        verify(addressLocalityService, times(1))
            .getBy(locality = any(), country = any(), region = any(), language = any(), scheme = any())
    }

    @Test
    fun `Getting the locality by code is error (country code is invalid)`() {

        doThrow(InvalidCountryCodeException(description = "Invalid country code: '$INVALID_COUNTRY' (wrong length: '${INVALID_COUNTRY.length}' required: '2')."))
            .whenever(addressLocalityService)
            .getBy(
                locality = eq(LOCALITY),
                country = eq(INVALID_COUNTRY),
                region = eq(REGION),
                language = eq(LANGUAGE),
                scheme = eq(SCHEME)
            )

        val url = getUrl(locality = LOCALITY, country = INVALID_COUNTRY, region = REGION)
        mockMvc.perform(
            get(url)
                .param("lang", LANGUAGE)
                .param("scheme", SCHEME)
        )
            .andExpect(status().isBadRequest)
            .andExpect(content().contentType("application/json;charset=UTF-8"))
            .andExpect(jsonPath("$.errors.length()", equalTo(1)))
            .andExpect(jsonPath("$.errors[0].code", equalTo(INVALID_COUNTRY_CODE.code)))
            .andExpect(
                jsonPath(
                    "$.errors[0].description",
                    equalTo("Invalid country code: '$INVALID_COUNTRY' (wrong length: '${INVALID_COUNTRY.length}' required: '2').")
                )
            )

        verify(addressLocalityService, times(1))
            .getBy(locality = any(), country = any(), region = any(), language = any(), scheme = any())
    }

    @Test
    fun `Getting the locality by code is error (region code is empty)`() {
        doThrow(InvalidRegionCodeException(description = "Invalid region code (value is blank)."))
            .whenever(addressLocalityService)
            .getBy(
                locality = eq(LOCALITY),
                country = eq(COUNTRY),
                region = eq(EMPTY_REGION),
                language = eq(LANGUAGE),
                scheme = eq(SCHEME)
            )

        val url = getUrl(locality = LOCALITY, country = COUNTRY, region = EMPTY_REGION)
        mockMvc.perform(
            get(url)
                .param("lang", LANGUAGE)
                .param("scheme", SCHEME)
        )
            .andExpect(status().isBadRequest)
            .andExpect(content().contentType("application/json;charset=UTF-8"))
            .andExpect(jsonPath("$.errors.length()", equalTo(1)))
            .andExpect(jsonPath("$.errors[0].code", equalTo(INVALID_REGION_CODE.code)))
            .andExpect(
                jsonPath(
                    "$.errors[0].description",
                    equalTo("Invalid region code (value is blank).")
                )
            )

        verify(addressLocalityService, times(1))
            .getBy(locality = any(), country = any(), region = any(), language = any(), scheme = any())
    }

    @Test
    fun `Getting the locality by code is error (locality code is empty)`() {
        doThrow(InvalidLocalityCodeException(description = "Invalid locality code (value is blank)."))
            .whenever(addressLocalityService)
            .getBy(
                locality = eq(EMPTY_LOCALITY),
                country = eq(COUNTRY),
                region = eq(REGION),
                language = eq(LANGUAGE),
                scheme = eq(SCHEME)
            )

        val url = getUrl(locality = EMPTY_LOCALITY, country = COUNTRY, region = REGION)
        mockMvc.perform(
            get(url)
                .param("lang", LANGUAGE)
                .param("scheme", SCHEME)
        )
            .andExpect(status().isBadRequest)
            .andExpect(content().contentType("application/json;charset=UTF-8"))
            .andExpect(jsonPath("$.errors.length()", equalTo(1)))
            .andExpect(jsonPath("$.errors[0].code", equalTo(INVALID_LOCALITY_CODE.code)))
            .andExpect(
                jsonPath(
                    "$.errors[0].description",
                    equalTo("Invalid locality code (value is blank).")
                )
            )

        verify(addressLocalityService, times(1))
            .getBy(locality = any(), country = any(), region = any(), language = any(), scheme = any())
    }

    @Test
    fun `Getting the locality by code is error (locality is not found)`() {
        doThrow(
            LocalityNotFoundException(
                locality = LocalityCode(LOCALITY),
                country = CountryCode(COUNTRY),
                region = RegionCode(REGION),
                language = LanguageCode(LANGUAGE),
                scheme = LocalityScheme(SCHEME)
            )
        )
            .whenever(addressLocalityService)
            .getBy(
                locality = eq(LOCALITY),
                country = eq(COUNTRY),
                region = eq(REGION),
                language = eq(LANGUAGE),
                scheme = eq(SCHEME)
            )

        val url = getUrl(locality = LOCALITY, country = COUNTRY, region = REGION)
        mockMvc.perform(
            get(url)
                .param("lang", LANGUAGE)
                .param("scheme", SCHEME)
        )
            .andExpect(status().isNotFound)
            .andExpect(content().contentType("application/json;charset=UTF-8"))
            .andExpect(jsonPath("$.errors.length()", equalTo(1)))
            .andExpect(jsonPath("$.errors[0].code", equalTo(LOCALITY_NOT_FOUND.code)))
            .andExpect(
                jsonPath(
                    "$.errors[0].description",
                    equalTo("The locality by code '$LOCALITY', scheme '$SCHEME', country '$COUNTRY', region '$REGION', language '$LANGUAGE' not found.")
                )
            )


        verify(addressLocalityService, times(1))
            .getBy(locality = any(), country = any(), region = any(), language = any(), scheme = any())
    }

    @Test
    fun `Getting the locality by code is error (internal server error)`() {
        doThrow(RuntimeException())
            .whenever(addressLocalityService)
            .getBy(
                locality = eq(LOCALITY),
                country = eq(COUNTRY),
                region = eq(REGION),
                language = eq(LANGUAGE),
                scheme = eq(SCHEME)
            )

        val url = getUrl(locality = LOCALITY, country = COUNTRY, region = REGION)
        mockMvc.perform(
            get(url)
                .param("lang", LANGUAGE)
                .param("scheme", SCHEME)
        )
            .andExpect(status().isInternalServerError)
            .andExpect(content().contentType("application/json;charset=UTF-8"))
            .andExpect(jsonPath("$.errors.length()", equalTo(1)))
            .andExpect(jsonPath("$.errors[0].code", equalTo(INTERNAL_SERVER_ERROR.code)))
            .andExpect(jsonPath("$.errors[0].description", equalTo("Internal server error.")))


        verify(addressLocalityService, times(1))
            .getBy(locality = any(), country = any(), region = any(), language = any(), scheme = any())
    }

    @Test
    fun `Getting all schemes of localities is successful`() {
        whenever(addressLocalityService.getAllSchemes(country = eq(COUNTRY), region = eq(REGION)))
            .thenReturn(listOf(SCHEME))

        val url = getUrlSchemesAttribute(country = COUNTRY, region = REGION)
        mockMvc.perform(
            get(url)
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType("application/json;charset=UTF-8"))
            .andExpect(jsonPath("$.data.schemes.length()", equalTo(1)))
            .andExpect(jsonPath("$.data.schemes[0]", equalTo(SCHEME)))

        verify(addressLocalityService, times(1))
            .getAllSchemes(country = any(), region = any())
    }

    @Test
    fun `Getting all schemes of localities is successful (list of using schemes are empty)`() {
        whenever(addressLocalityService.getAllSchemes(country = eq(COUNTRY), region = eq(REGION)))
            .thenReturn(listOf())

        val url = getUrlSchemesAttribute(country = COUNTRY, region = REGION)
        mockMvc.perform(
            get(url)
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType("application/json;charset=UTF-8"))
            .andExpect(jsonPath("$.data.schemes.length()", equalTo(0)))

        verify(addressLocalityService, times(1))
            .getAllSchemes(country = any(), region = any())
    }

    private fun getUrl(locality: String? = null, country: String, region: String): String =
        if (locality == null)
            String.format("/addresses/countries/%s/regions/%s/localities", country, region)
        else
            String.format("/addresses/countries/%s/regions/%s/localities/%s", country, region, locality)

    private fun getUrlSchemesAttribute(country: String, region: String): String =
        String.format("/addresses/countries/%s/regions/%s/localities/attributes/schemes", country, region)
}
