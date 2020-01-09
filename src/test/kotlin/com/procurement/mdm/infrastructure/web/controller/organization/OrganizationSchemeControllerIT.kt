package com.procurement.mdm.infrastructure.web.controller.organization

import com.procurement.mdm.application.service.organization.OrganizationSchemeServiceImpl
import com.procurement.mdm.domain.repository.address.AddressCountryRepository
import com.procurement.mdm.domain.repository.organization.OrganizationSchemeRepository
import com.procurement.mdm.infrastructure.repository.AbstractRepositoryTest
import com.procurement.mdm.infrastructure.repository.loadSql
import com.procurement.mdm.infrastructure.web.controller.RestExceptionHandler
import com.procurement.mdm.infrastructure.web.controller.documentation.ModelDescription
import com.procurement.mdm.infrastructure.web.dto.ErrorCode.COUNTRY_REQUEST_PARAMETER_MISSING
import com.procurement.mdm.infrastructure.web.dto.ErrorCode.COUNTRY_REQUEST_PARAMETER_UNKNOWN
import com.procurement.mdm.infrastructure.web.dto.ErrorCode.INVALID_COUNTRY_CODE
import com.procurement.mdm.infrastructure.web.dto.ErrorCode.ORGANIZATION_SCHEME_NOT_FOUND
import com.procurement.mdm.infrastructure.web.dto.ErrorCode.REQUEST_PAYLOAD_MISSING
import org.hamcrest.Matchers.contains
import org.hamcrest.core.IsEqual.equalTo
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.RestDocumentationExtension
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration
import org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.test.web.servlet.setup.StandaloneMockMvcBuilder

@ExtendWith(RestDocumentationExtension::class)
@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
class OrganizationSchemeControllerIT : AbstractRepositoryTest() {
    companion object {
        private const val COUNTRY = "md"
        private const val EMPTY_COUNTRY = "   "
        private const val INVALID_COUNTRY = "INVALID_COUNTRY"
        private const val UNKNOWN_COUNTRY = "uc"
    }

    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var organizationSchemeRepository: OrganizationSchemeRepository

    @Autowired
    private lateinit var addressCountryRepository: AddressCountryRepository

    @BeforeAll
    fun setup(restDocumentation: RestDocumentationContextProvider) {
        val organizationSchemeService = OrganizationSchemeServiceImpl(
            organizationSchemeRepository = organizationSchemeRepository,
            addressCountryRepository = addressCountryRepository
        )

        val controller = OrganizationSchemeController(organizationSchemeService)
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

    @Nested
    inner class SchemesByCountry {
        @Test
        fun `Getting the organization schemes for country is successful`() {
            initData()

            val url = getUrl()
            mockMvc.perform(
                get(url)
                    .param("country", COUNTRY)
            )
                .andExpect(status().isOk)
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.data.schemes[*]", contains("ISO", "CUATM")))
                .andDo(
                    document(
                        "organization/schemes/find_all_only_code/success",
                        responseFields(ModelDescription.Organization.Scheme.codes())
                    )
                )
        }

        @Test
        fun `Getting the organization schemes for country is error (country request parameter is missing)`() {
            initData()

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
                        "organization/schemes/find_all_only_code/errors/no_country_query_param",
                        responseFields(ModelDescription.responseError())
                    )
                )
        }

        @Test
        fun `Getting the organization schemes for country is error (country code is empty)`() {
            initData()

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
                        "organization/schemes/get_by_code/errors/empty_country",
                        responseFields(ModelDescription.responseError())
                    )
                )
        }

        @Test
        fun `Getting the organization schemes for country is error (country code is invalid)`() {
            initData()

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
                        "organization/schemes/get_by_code/errors/invalid_country",
                        responseFields(ModelDescription.responseError())
                    )
                )
        }

        @Test
        fun `Getting the organization schemes for country is error (country request parameter is unknown)`() {
            initData()

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
                        "organization/schemes/find_all_only_code/errors/unknown_country",
                        responseFields(ModelDescription.responseError())
                    )
                )
        }

        @Test
        fun `Getting the organization schemes for country is error (organization schemes not found)`() {
            initSchemes()
            initCountries()

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
                .andDo(
                    document(
                        "organization/schemes/find_all_only_code/errors/organization_scheme_not_found",
                        responseFields(ModelDescription.responseError())
                    )
                )
        }
    }

    @Nested
    inner class SchemesByCountries {
        @Test
        fun `Getting the organization schemes for countries is successful`() {
            initData()

            val url = getUrl()
            mockMvc.perform(
                post(url)
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content("""{"countries": ["$COUNTRY"]}""")
            )
                .andExpect(status().isOk)
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.data.elements[0].schemes[*]", contains("ISO", "CUATM")))
                .andDo(
                    document(
                        "organization/schemes/find_all_only_code/success",
                        responseFields(ModelDescription.Organization.Scheme.elements())
                    )
                )
        }

        @Test
        fun `Getting the organization schemes for country is error (payload is missing)`() {
            initData()

            val url = getUrl()
            mockMvc.perform(
                post(url)
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
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
                .andDo(
                    document(
                        "organization/schemes/find_all_only_code/errors/no_country_query_param",
                        responseFields(ModelDescription.responseError())
                    )
                )
        }

        @Test
        fun `Getting the organization schemes for country is error (country code is empty)`() {
            initData()

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
                .andDo(
                    document(
                        "organization/schemes/get_by_code/errors/empty_country",
                        responseFields(ModelDescription.responseError())
                    )
                )
        }

        @Test
        fun `Getting the organization schemes for country is error (country code is invalid)`() {
            initData()

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
                .andDo(
                    document(
                        "organization/schemes/get_by_code/errors/invalid_country",
                        responseFields(ModelDescription.responseError())
                    )
                )
        }

        @Test
        fun `Getting the organization schemes for country is error (country code is unknown)`() {
            initData()

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
                .andDo(
                    document(
                        "organization/schemes/find_all_only_code/errors/unknown_country",
                        responseFields(ModelDescription.responseError())
                    )
                )
        }

        @Test
        fun `Getting the organization schemes for country is error (organization schemes not found)`() {
            initSchemes()
            initCountries()

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
                .andDo(
                    document(
                        "organization/schemes/find_all_only_code/errors/organization_scheme_not_found",
                        responseFields(ModelDescription.responseError())
                    )
                )
        }
    }

    private fun initData() {
        initSchemes()

        initCountries()

        val sqlSchemes = loadSql("sql/organization/schemes_init_data.sql")
        executeSQLScript(sqlSchemes)
    }

    private fun initSchemes() {
        val sqlSchemes = loadSql("sql/list_schemes_init_data.sql")
        executeSQLScript(sqlSchemes)
    }

    private fun initCountries() {
        val sqlCountries = loadSql("sql/address/countries_init_data.sql")
        executeSQLScript(sqlCountries)
    }

    private fun getUrl(): String = String.format("/organization/schemes")
}
