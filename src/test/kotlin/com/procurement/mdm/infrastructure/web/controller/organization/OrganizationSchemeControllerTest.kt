package com.procurement.mdm.infrastructure.web.controller.organization

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doThrow
import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.reset
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.procurement.mdm.application.exception.OrganizationSchemeNotFoundException
import com.procurement.mdm.application.service.organization.OrganizationSchemeService
import com.procurement.mdm.domain.exception.CountryUnknownException
import com.procurement.mdm.domain.exception.InvalidCountryCodeException
import com.procurement.mdm.domain.model.code.CountryCode
import com.procurement.mdm.infrastructure.web.controller.RestExceptionHandler
import com.procurement.mdm.infrastructure.web.dto.ErrorCode.COUNTRY_REQUEST_PARAMETER_MISSING
import com.procurement.mdm.infrastructure.web.dto.ErrorCode.COUNTRY_REQUEST_PARAMETER_UNKNOWN
import com.procurement.mdm.infrastructure.web.dto.ErrorCode.INTERNAL_SERVER_ERROR
import com.procurement.mdm.infrastructure.web.dto.ErrorCode.INVALID_COUNTRY_CODE
import com.procurement.mdm.infrastructure.web.dto.ErrorCode.ORGANIZATION_SCHEME_NOT_FOUND
import com.procurement.mdm.infrastructure.web.dto.ErrorCode.REQUEST_PAYLOAD_MISSING
import org.hamcrest.Matchers.contains
import org.hamcrest.core.IsEqual.equalTo
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders

class OrganizationSchemeControllerTest {
    companion object {
        private const val COUNTRY = "md"
        private const val EMPTY_COUNTRY = "   "
        private const val INVALID_COUNTRY = "INVALID_COUNTRY"
        private const val UNKNOWN_COUNTRY = "uc"

        private const val SCHEME_CODE_FIRST = "ISO"
        private const val SCHEME_CODE_SECOND = "CUATM"
        private val SCHEMES_CODES = listOf(SCHEME_CODE_FIRST, SCHEME_CODE_SECOND)
    }

    private val organizationSchemeService: OrganizationSchemeService = mock()
    private val mockMvc: MockMvc =
        MockMvcBuilders.standaloneSetup(OrganizationSchemeController(organizationSchemeService))
            .setControllerAdvice(RestExceptionHandler())
            .build()

    @BeforeEach
    fun init() {
        reset(organizationSchemeService)
    }

    @Nested
    inner class SchemesByCountry {
        @Test
        fun `Getting the organization schemes for country is successful`() {
            whenever(organizationSchemeService.find(country = eq(COUNTRY)))
                .thenReturn(SCHEMES_CODES)

            val url = getUrl()
            mockMvc.perform(
                get(url)
                    .param("country", COUNTRY)
            )
                .andExpect(status().isOk)
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.data.schemes[0]", equalTo(SCHEME_CODE_FIRST)))
                .andExpect(jsonPath("$.data.schemes[1]", equalTo(SCHEME_CODE_SECOND)))

            verify(organizationSchemeService, times(1))
                .find(country = any())
        }

        @Test
        fun `Getting the organization schemes for country is error (country request parameter is missing)`() {
            val url = getUrl()
            mockMvc.perform(
                get(url)
            )
                .andExpect(status().isBadRequest)
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.errors.length()", equalTo(1)))
                .andExpect(jsonPath("$.errors[0].code", equalTo(COUNTRY_REQUEST_PARAMETER_MISSING.code)))
                .andExpect(
                    jsonPath(
                        "$.errors[0].description",
                        equalTo("The request is missing a required query parameter - 'country'.")
                    )
                )

            verify(organizationSchemeService, times(0))
                .find(country = any())
        }

        @Test
        fun `Getting the organization schemes for country is error (country code is empty)`() {
            doThrow(InvalidCountryCodeException(description = "Invalid country code (value is blank)."))
                .whenever(organizationSchemeService)
                .find(country = eq(EMPTY_COUNTRY))

            val url = getUrl()
            mockMvc.perform(
                get(url)
                    .param("country", EMPTY_COUNTRY)
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

            verify(organizationSchemeService, times(1))
                .find(country = any())
        }

        @Test
        fun `Getting the organization schemes for country is error (country code is invalid)`() {

            doThrow(InvalidCountryCodeException(description = "Invalid country code: '$INVALID_COUNTRY' (wrong length: '${INVALID_COUNTRY.length}' required: '2')."))
                .whenever(organizationSchemeService)
                .find(country = eq(INVALID_COUNTRY))

            val url = getUrl()
            mockMvc.perform(
                get(url)
                    .param("country", INVALID_COUNTRY)
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

            verify(organizationSchemeService, times(1))
                .find(country = any())
        }

        @Test
        fun `Getting the organization schemes for country is error (country request parameter is unknown)`() {
            doThrow(CountryUnknownException(country = UNKNOWN_COUNTRY))
                .whenever(organizationSchemeService)
                .find(country = eq(UNKNOWN_COUNTRY))

            val url = getUrl()
            mockMvc.perform(
                get(url)
                    .param("country", UNKNOWN_COUNTRY)
            )
                .andExpect(status().isBadRequest)
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.errors.length()", equalTo(1)))
                .andExpect(jsonPath("$.errors[0].code", equalTo(COUNTRY_REQUEST_PARAMETER_UNKNOWN.code)))
                .andExpect(
                    jsonPath(
                        "$.errors[0].description",
                        equalTo("The unknown code of a country '$UNKNOWN_COUNTRY'.")
                    )
                )

            verify(organizationSchemeService, times(1))
                .find(country = any())
        }

        @Test
        fun `Getting the organization schemes for country is error (organization schemes not found)`() {
            doThrow(OrganizationSchemeNotFoundException(country = COUNTRY))
                .whenever(organizationSchemeService)
                .find(country = eq(COUNTRY))

            val url = getUrl()
            mockMvc.perform(
                get(url)
                    .param("country", COUNTRY)
            )
                .andExpect(status().isNotFound)
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.errors.length()", equalTo(1)))
                .andExpect(jsonPath("$.errors[0].code", equalTo(ORGANIZATION_SCHEME_NOT_FOUND.code)))
                .andExpect(
                    jsonPath(
                        "$.errors[0].description",
                        equalTo("The organization schemes for country '$COUNTRY' not found.")
                    )
                )

            verify(organizationSchemeService, times(1))
                .find(country = any())
        }

        @Test
        fun `Getting the organization schemes for country is error (internal server error)`() {
            doThrow(RuntimeException())
                .whenever(organizationSchemeService)
                .find(country = eq(COUNTRY))

            val url = getUrl()
            mockMvc.perform(
                get(url)
                    .param("country", COUNTRY)
            )
                .andExpect(status().isInternalServerError)
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.errors.length()", equalTo(1)))
                .andExpect(jsonPath("$.errors[0].code", equalTo(INTERNAL_SERVER_ERROR.code)))
                .andExpect(jsonPath("$.errors[0].description", equalTo("Internal server error.")))

            verify(organizationSchemeService, times(1))
                .find(country = any())
        }
    }

    @Nested
    inner class SchemesByCountries {
        @Test
        fun `Getting the organization schemes for countries is successful`() {
            whenever(organizationSchemeService.find(countries = eq(listOf(COUNTRY))))
                .thenReturn(
                    mapOf(CountryCode(COUNTRY) to listOf(SCHEME_CODE_FIRST, SCHEME_CODE_SECOND))
                )

            val url = getUrl()
            val l = mockMvc.perform(
                post(url)
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content("""{"countries": ["$COUNTRY"]}""")
            )
                .andExpect(status().isOk)
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.data.elements.length()", equalTo(1)))
                .andExpect(jsonPath("$.data.elements[0].country", equalTo(COUNTRY)))
                .andExpect(jsonPath("$.data.elements[0].schemes", contains(SCHEME_CODE_FIRST, SCHEME_CODE_SECOND)))
                .andReturn()

            println(l.response.contentAsString)
            verify(organizationSchemeService, times(1))
                .find(countries = any())
        }

        @Test
        fun `Getting the organization schemes for country is error (payload is missing)`() {
            val url = getUrl()
            mockMvc.perform(
                post(url)
            )
                .andExpect(status().isBadRequest)
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.errors.length()", equalTo(1)))
                .andExpect(jsonPath("$.errors[0].code", equalTo(REQUEST_PAYLOAD_MISSING.code)))
                .andExpect(
                    jsonPath(
                        "$.errors[0].description",
                        equalTo("The request is missing a required payload.")
                    )
                )

            verify(organizationSchemeService, times(0))
                .find(countries = any())
        }

        @Test
        fun `Getting the organization schemes for country is error (country code is empty)`() {
            doThrow(InvalidCountryCodeException(description = "Invalid country code (value is blank)."))
                .whenever(organizationSchemeService)
                .find(countries = eq(listOf(EMPTY_COUNTRY)))

            val url = getUrl()
            mockMvc.perform(
                post(url)
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content("""{"countries": ["$EMPTY_COUNTRY"]}""")
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

            verify(organizationSchemeService, times(1))
                .find(countries = any())
        }

        @Test
        fun `Getting the organization schemes for country is error (country code is invalid)`() {

            doThrow(InvalidCountryCodeException(description = "Invalid country code: '$INVALID_COUNTRY' (wrong length: '${INVALID_COUNTRY.length}' required: '2')."))
                .whenever(organizationSchemeService)
                .find(countries = eq(listOf(INVALID_COUNTRY)))

            val url = getUrl()
            mockMvc.perform(
                post(url)
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content("""{"countries": ["$INVALID_COUNTRY"]}""")
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

            verify(organizationSchemeService, times(1))
                .find(countries = any())
        }

        @Test
        fun `Getting the organization schemes for country is error (country code is unknown)`() {
            doThrow(CountryUnknownException(country = UNKNOWN_COUNTRY))
                .whenever(organizationSchemeService)
                .find(countries = eq(listOf(UNKNOWN_COUNTRY)))

            val url = getUrl()
            mockMvc.perform(
                post(url)
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content("""{"countries": ["$UNKNOWN_COUNTRY"]}""")
            )
                .andExpect(status().isBadRequest)
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.errors.length()", equalTo(1)))
                .andExpect(jsonPath("$.errors[0].code", equalTo(COUNTRY_REQUEST_PARAMETER_UNKNOWN.code)))
                .andExpect(
                    jsonPath(
                        "$.errors[0].description",
                        equalTo("The unknown code of a country '$UNKNOWN_COUNTRY'.")
                    )
                )

            verify(organizationSchemeService, times(1))
                .find(countries = any())
        }

        @Test
        fun `Getting the organization schemes for country is error (organization schemes not found)`() {
            doThrow(OrganizationSchemeNotFoundException(country = COUNTRY))
                .whenever(organizationSchemeService)
                .find(countries = eq(listOf(COUNTRY)))

            val url = getUrl()
            mockMvc.perform(
                post(url)
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content("""{"countries": ["$COUNTRY"]}""")
            )
                .andExpect(status().isNotFound)
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.errors.length()", equalTo(1)))
                .andExpect(jsonPath("$.errors[0].code", equalTo(ORGANIZATION_SCHEME_NOT_FOUND.code)))
                .andExpect(
                    jsonPath(
                        "$.errors[0].description",
                        equalTo("The organization schemes for country '$COUNTRY' not found.")
                    )
                )

            verify(organizationSchemeService, times(1))
                .find(countries = any())
        }

        @Test
        fun `Getting the organization schemes for country is error (internal server error)`() {
            doThrow(RuntimeException())
                .whenever(organizationSchemeService)
                .find(countries = eq(listOf(COUNTRY)))

            val url = getUrl()
            mockMvc.perform(
                post(url)
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content("""{"countries": ["$COUNTRY"]}""")
            )
                .andExpect(status().isInternalServerError)
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.errors.length()", equalTo(1)))
                .andExpect(jsonPath("$.errors[0].code", equalTo(INTERNAL_SERVER_ERROR.code)))
                .andExpect(jsonPath("$.errors[0].description", equalTo("Internal server error.")))

            verify(organizationSchemeService, times(1))
                .find(countries = any())
        }
    }

    private fun getUrl(): String = String.format("/organization/schemes")
}
