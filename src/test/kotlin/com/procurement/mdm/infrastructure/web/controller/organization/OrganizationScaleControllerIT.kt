package com.procurement.mdm.infrastructure.web.controller.organization

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doThrow
import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.procurement.mdm.application.exception.OrganizationScaleNotFoundException
import com.procurement.mdm.application.service.organization.OrganizationScaleService
import com.procurement.mdm.domain.exception.CountryUnknownException
import com.procurement.mdm.domain.exception.InvalidCountryCodeException
import com.procurement.mdm.infrastructure.web.controller.RestExceptionHandler
import com.procurement.mdm.infrastructure.web.controller.documentation.ModelDescription
import com.procurement.mdm.infrastructure.web.dto.ErrorCode.COUNTRY_REQUEST_PARAMETER_MISSING
import com.procurement.mdm.infrastructure.web.dto.ErrorCode.COUNTRY_REQUEST_PARAMETER_UNKNOWN
import com.procurement.mdm.infrastructure.web.dto.ErrorCode.INVALID_COUNTRY_CODE
import com.procurement.mdm.infrastructure.web.dto.ErrorCode.ORGANIZATION_SCALE_NOT_FOUND
import org.hamcrest.core.IsEqual.equalTo
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.RestDocumentationExtension
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration
import org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.test.web.servlet.setup.StandaloneMockMvcBuilder

@ExtendWith(RestDocumentationExtension::class)
class OrganizationScaleControllerIT {
    companion object {
        private const val COUNTRY = "md"
        private const val EMPTY_COUNTRY = "   "
        private const val INVALID_COUNTRY = "INVALID_COUNTRY"
        private const val UNKNOWN_COUNTRY = "uc"

        private const val SCALE_CODE_FIRST = "MICRO"
        private const val SCALE_CODE_SECOND = "LARGE"
        private val SCALES_CODES = listOf(SCALE_CODE_FIRST, SCALE_CODE_SECOND)
    }

    private lateinit var mockMvc: MockMvc
    private lateinit var organizationScaleService: OrganizationScaleService

    @BeforeEach
    fun init(restDocumentation: RestDocumentationContextProvider) {
        organizationScaleService = mock()

        val controller = OrganizationScaleController(organizationScaleService)
        val restExceptionHandler = RestExceptionHandler()
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
            .setControllerAdvice(restExceptionHandler)
            .apply<StandaloneMockMvcBuilder>(
                documentationConfiguration(restDocumentation)
                    .uris()
                    .withScheme("https")
                    .withHost("eprocurement.systems")
                    .and()
                    .snippets()
                    .and()
                    .operationPreprocessors()
                    .withRequestDefaults(prettyPrint())
                    .withResponseDefaults(prettyPrint())
            )
            .build()
    }

    @Test
    fun `Getting the organization scales for country is successful`() {
        whenever(organizationScaleService.findAllOnlyCode(country = eq(COUNTRY)))
            .thenReturn(SCALES_CODES)

        val url = getUrl()
        mockMvc.perform(
            get(url)
                .param("country", COUNTRY)
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType("application/json;charset=UTF-8"))
            .andExpect(jsonPath("$.data.scales[0]", equalTo(SCALE_CODE_FIRST)))
            .andExpect(jsonPath("$.data.scales[1]", equalTo(SCALE_CODE_SECOND)))
            .andDo(
                document(
                    "organization/scales/find_all_only_code/success",
                    responseFields(ModelDescription.Organization.Scale.codes())
                )
            )

        verify(organizationScaleService, times(1))
            .findAllOnlyCode(country = any())
    }

    @Test
    fun `Getting the organization scales for country is error (country request parameter is missing)`() {
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
            .andDo(
                document(
                    "organization/scales/find_all_only_code/errors/no_country_query_param",
                    responseFields(ModelDescription.responseError())
                )
            )

        verify(organizationScaleService, times(0))
            .findAllOnlyCode(country = any())
    }

    @Test
    fun `Getting the organization scales for country is error (country code is empty)`() {
        doThrow(InvalidCountryCodeException(description = "Invalid country code (value is blank)."))
            .whenever(organizationScaleService)
            .findAllOnlyCode(country = eq(EMPTY_COUNTRY))

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
            .andDo(
                document(
                    "organization/scales/get_by_code/errors/empty_country",
                    responseFields(ModelDescription.responseError())
                )
            )

        verify(organizationScaleService, times(1))
            .findAllOnlyCode(country = any())
    }

    @Test
    fun `Getting the organization scales for country is error (country code is invalid)`() {

        doThrow(InvalidCountryCodeException(description = "Invalid country code: '$INVALID_COUNTRY' (wrong length: '${INVALID_COUNTRY.length}' required: '2')."))
            .whenever(organizationScaleService)
            .findAllOnlyCode(country = eq(INVALID_COUNTRY))

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
            .andDo(
                document(
                    "organization/scales/get_by_code/errors/invalid_country",
                    responseFields(ModelDescription.responseError())
                )
            )

        verify(organizationScaleService, times(1))
            .findAllOnlyCode(country = any())
    }

    @Test
    fun `Getting the organization scales for country is error (country request parameter is unknown)`() {
        doThrow(CountryUnknownException(country = UNKNOWN_COUNTRY))
            .whenever(organizationScaleService)
            .findAllOnlyCode(country = eq(UNKNOWN_COUNTRY))

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
            .andDo(
                document(
                    "organization/scales/find_all_only_code/errors/unknown_country",
                    responseFields(ModelDescription.responseError())
                )
            )

        verify(organizationScaleService, times(1))
            .findAllOnlyCode(country = any())
    }

    @Test
    fun `Getting the organization scales for country is error (organization schemes not found)`() {
        doThrow(OrganizationScaleNotFoundException(country = COUNTRY))
            .whenever(organizationScaleService)
            .findAllOnlyCode(country = eq(COUNTRY))

        val url = getUrl()
        mockMvc.perform(
            get(url)
                .param("country", COUNTRY)
        )
            .andExpect(status().isNotFound)
            .andExpect(content().contentType("application/json;charset=UTF-8"))
            .andExpect(jsonPath("$.errors.length()", equalTo(1)))
            .andExpect(jsonPath("$.errors[0].code", equalTo(ORGANIZATION_SCALE_NOT_FOUND.code)))
            .andExpect(
                jsonPath(
                    "$.errors[0].description",
                    equalTo("The organization scale for country '$COUNTRY' not found.")
                )
            )
            .andDo(
                document(
                    "organization/scales/find_all_only_code/errors/organization_scale_not_found",
                    responseFields(ModelDescription.responseError())
                )
            )

        verify(organizationScaleService, times(1))
            .findAllOnlyCode(country = any())
    }

    private fun getUrl(): String = String.format("/organization/scales")
}
