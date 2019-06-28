package com.procurement.mdm.infrastructure.web.controller.address

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doThrow
import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.procurement.mdm.application.exception.RegionNotFoundException
import com.procurement.mdm.application.service.address.AddressRegionService
import com.procurement.mdm.domain.exception.InvalidCountryCodeException
import com.procurement.mdm.domain.exception.InvalidLanguageCodeException
import com.procurement.mdm.domain.exception.InvalidRegionCodeException
import com.procurement.mdm.domain.exception.LanguageUnknownException
import com.procurement.mdm.domain.model.identifier.RegionIdentifier
import com.procurement.mdm.infrastructure.web.controller.RestExceptionHandler
import com.procurement.mdm.infrastructure.web.dto.ErrorCode.INTERNAL_SERVER_ERROR
import com.procurement.mdm.infrastructure.web.dto.ErrorCode.INVALID_COUNTRY_CODE
import com.procurement.mdm.infrastructure.web.dto.ErrorCode.INVALID_LANGUAGE_CODE
import com.procurement.mdm.infrastructure.web.dto.ErrorCode.INVALID_REGION_CODE
import com.procurement.mdm.infrastructure.web.dto.ErrorCode.LANGUAGE_REQUEST_PARAMETER_MISSING
import com.procurement.mdm.infrastructure.web.dto.ErrorCode.LANGUAGE_REQUEST_PARAMETER_UNKNOWN
import com.procurement.mdm.infrastructure.web.dto.ErrorCode.REGION_NOT_FOUND
import org.hamcrest.core.IsEqual.equalTo
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders

class AddressRegionControllerTest {
    companion object {
        private const val COUNTRY = "md"
        private const val EMPTY_COUNTRY = "   "
        private const val INVALID_COUNTRY = "INVALID_COUNTRY"

        private const val REGION = "REGION-1"
        private const val EMPTY_REGION = "   "
        private const val INVALID_REGION = "INVALID_REGION"

        private const val LANGUAGE = "ro"
        private const val EMPTY_LANGUAGE = "   "
        private const val INVALID_LANGUAGE = "invalid"
        private const val UNKNOWN_LANGUAGE = "ul"
    }

    private lateinit var mockMvc: MockMvc
    private lateinit var addressRegionService: AddressRegionService

    @BeforeEach
    fun init() {
        addressRegionService = mock()

        val controller = AddressRegionController(addressRegionService)
        val restExceptionHandler = RestExceptionHandler()
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
            .setControllerAdvice(restExceptionHandler)
            .build()
    }

    @Test
    fun `Getting the region by code is successful`() {
        val expected = RegionIdentifier(
            scheme = "scheme-1",
            id = REGION,
            description = "description-1",
            uri = "https://example-1.com"
        )

        whenever(addressRegionService.getBy(region = eq(REGION), country = eq(COUNTRY), language = eq(LANGUAGE)))
            .thenReturn(expected)

        val url = getUrl(region = REGION, country = COUNTRY)
        mockMvc.perform(
            get(url)
                .param("language", LANGUAGE)
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType("application/json;charset=UTF-8"))
            .andExpect(jsonPath("$.data.scheme", equalTo(expected.scheme)))
            .andExpect(jsonPath("$.data.id", equalTo(expected.id)))
            .andExpect(jsonPath("$.data.description", equalTo(expected.description)))
            .andExpect(jsonPath("$.data.uri", equalTo(expected.uri)))

        verify(addressRegionService, times(1))
            .getBy(region = any(), country = any(), language = any())
    }

    @Test
    fun `Getting the region by code is error (language request parameter is missing)`() {
        val url = getUrl(region = REGION, country = COUNTRY)
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
                    equalTo("The request is missing a required query parameter - 'language'.")
                )
            )

        verify(addressRegionService, times(0))
            .getBy(region = any(), country = any(), language = any())
    }

    @Test
    fun `Getting the region by code is error (language request parameter is empty)`() {
        doThrow(InvalidLanguageCodeException(description = "Invalid language code (value is blank)."))
            .whenever(addressRegionService)
            .getBy(region = eq(REGION), country = eq(COUNTRY), language = eq(EMPTY_LANGUAGE))

        val url = getUrl(country = COUNTRY, region = REGION)
        mockMvc.perform(
            get(url)
                .param("language", EMPTY_LANGUAGE)
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

        verify(addressRegionService, times(1))
            .getBy(country = any(), region = any(), language = any())
    }

    @Test
    fun `Getting the region by code is error (language request parameter is invalid)`() {
        doThrow(InvalidLanguageCodeException(description = "Invalid language code: '$INVALID_LANGUAGE' (wrong length: '${INVALID_LANGUAGE.length}' required: '2')."))
            .whenever(addressRegionService)
            .getBy(region = eq(REGION), country = eq(COUNTRY), language = eq(INVALID_LANGUAGE))

        val url = getUrl(country = COUNTRY, region = REGION)
        mockMvc.perform(
            get(url)
                .param("language", INVALID_LANGUAGE)
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

        verify(addressRegionService, times(1))
            .getBy(country = any(), region = any(), language = any())
    }

    @Test
    fun `Getting the region by code is error (language request parameter is unknown)`() {
        doThrow(LanguageUnknownException(language = UNKNOWN_LANGUAGE))
            .whenever(addressRegionService)
            .getBy(region = eq(REGION), country = eq(COUNTRY), language = eq(UNKNOWN_LANGUAGE))

        val url = getUrl(region = REGION, country = COUNTRY)
        mockMvc.perform(
            get(url)
                .param("language", UNKNOWN_LANGUAGE)
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

        verify(addressRegionService, times(1))
            .getBy(region = any(), country = any(), language = any())
    }

    @Test
    fun `Getting the region by code is error (country code is empty)`() {
        doThrow(InvalidCountryCodeException(description = "Invalid country code (value is blank)."))
            .whenever(addressRegionService)
            .getBy(region = eq(REGION), country = eq(EMPTY_COUNTRY), language = eq(LANGUAGE))

        val url = getUrl(region = REGION, country = EMPTY_COUNTRY)
        mockMvc.perform(
            get(url)
                .param("language", LANGUAGE)
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

        verify(addressRegionService, times(1))
            .getBy(region = any(), country = any(), language = any())
    }

    @Test
    fun `Getting the region by code is error (country code is invalid)`() {

        doThrow(InvalidCountryCodeException(description = "Invalid country code: '$INVALID_COUNTRY' (wrong length: '${INVALID_COUNTRY.length}' required: '2')."))
            .whenever(addressRegionService)
            .getBy(region = eq(REGION), country = eq(INVALID_COUNTRY), language = eq(LANGUAGE))

        val url = getUrl(region = REGION, country = INVALID_COUNTRY)
        mockMvc.perform(
            get(url)
                .param("language", LANGUAGE)
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

        verify(addressRegionService, times(1))
            .getBy(region = any(), country = any(), language = any())
    }

    @Test
    fun `Getting the region by code is error (region code is empty)`() {
        doThrow(InvalidRegionCodeException(description = "Invalid region code (value is blank)."))
            .whenever(addressRegionService)
            .getBy(region = eq(EMPTY_REGION), country = eq(COUNTRY), language = eq(LANGUAGE))

        val url = getUrl(region = EMPTY_REGION, country = COUNTRY)
        mockMvc.perform(
            get(url)
                .param("language", LANGUAGE)
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

        verify(addressRegionService, times(1))
            .getBy(region = any(), country = any(), language = any())
    }

    @Test
    fun `Getting the region by code is error (region code is invalid)`() {

        doThrow(InvalidRegionCodeException(description = "Invalid region code: '$INVALID_REGION' (wrong length: '${INVALID_REGION.length}' required: '2')."))
            .whenever(addressRegionService)
            .getBy(region = eq(INVALID_REGION), country = eq(COUNTRY), language = eq(LANGUAGE))

        val url = getUrl(region = INVALID_REGION, country = COUNTRY)
        mockMvc.perform(
            get(url)
                .param("language", LANGUAGE)
        )
            .andExpect(status().isBadRequest)
            .andExpect(content().contentType("application/json;charset=UTF-8"))
            .andExpect(jsonPath("$.errors.length()", equalTo(1)))
            .andExpect(jsonPath("$.errors[0].code", equalTo(INVALID_REGION_CODE.code)))
            .andExpect(
                jsonPath(
                    "$.errors[0].description",
                    equalTo("Invalid region code: '$INVALID_REGION' (wrong length: '${INVALID_REGION.length}' required: '2').")
                )
            )

        verify(addressRegionService, times(1))
            .getBy(region = any(), country = any(), language = any())
    }

    @Test
    fun `Getting the region by code is error (region is not found)`() {
        doThrow(RegionNotFoundException(region = REGION, country = COUNTRY, language = LANGUAGE))
            .whenever(addressRegionService)
            .getBy(region = eq(REGION), country = eq(COUNTRY), language = eq(LANGUAGE))

        val url = getUrl(region = REGION, country = COUNTRY)
        mockMvc.perform(
            get(url)
                .param("language", LANGUAGE)
        )
            .andExpect(status().isNotFound)
            .andExpect(content().contentType("application/json;charset=UTF-8"))
            .andExpect(jsonPath("$.errors.length()", equalTo(1)))
            .andExpect(jsonPath("$.errors[0].code", equalTo(REGION_NOT_FOUND.code)))
            .andExpect(
                jsonPath(
                    "$.errors[0].description",
                    equalTo("The region by code '$REGION', country '$COUNTRY', language '$LANGUAGE' not found.")
                )
            )


        verify(addressRegionService, times(1))
            .getBy(region = any(), country = any(), language = any())
    }

    @Test
    fun `Getting the region by code is error (internal server error)`() {
        doThrow(RuntimeException())
            .whenever(addressRegionService)
            .getBy(region = eq(REGION), country = eq(COUNTRY), language = eq(LANGUAGE))

        val url = getUrl(region = REGION, country = COUNTRY)
        mockMvc.perform(
            get(url)
                .param("language", LANGUAGE)
        )
            .andExpect(status().isInternalServerError)
            .andExpect(content().contentType("application/json;charset=UTF-8"))
            .andExpect(jsonPath("$.errors.length()", equalTo(1)))
            .andExpect(jsonPath("$.errors[0].code", equalTo(INTERNAL_SERVER_ERROR.code)))
            .andExpect(jsonPath("$.errors[0].description", equalTo("Internal server error.")))

        verify(addressRegionService, times(1))
            .getBy(region = any(), country = any(), language = any())
    }

    private fun getUrl(region: String, country: String): String =
        String.format("/addresses/countries/%s/regions/%s", country, region)
}
