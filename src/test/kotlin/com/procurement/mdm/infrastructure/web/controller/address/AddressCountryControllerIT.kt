package com.procurement.mdm.infrastructure.web.controller.address

import com.procurement.mdm.application.service.address.AddressCountryService
import com.procurement.mdm.application.service.address.AddressCountryServiceImpl
import com.procurement.mdm.domain.model.identifier.CountryIdentifier
import com.procurement.mdm.domain.repository.AdvancedLanguageRepository
import com.procurement.mdm.domain.repository.address.AddressCountryRepository
import com.procurement.mdm.domain.repository.scheme.CountrySchemeRepository
import com.procurement.mdm.infrastructure.repository.AbstractRepositoryTest
import com.procurement.mdm.infrastructure.repository.loadSql
import com.procurement.mdm.infrastructure.web.controller.RestExceptionHandler
import com.procurement.mdm.infrastructure.web.controller.documentation.ModelDescription
import com.procurement.mdm.infrastructure.web.dto.ErrorCode.COUNTRY_NOT_FOUND
import com.procurement.mdm.infrastructure.web.dto.ErrorCode.INVALID_COUNTRY_CODE
import com.procurement.mdm.infrastructure.web.dto.ErrorCode.INVALID_LANGUAGE_CODE
import com.procurement.mdm.infrastructure.web.dto.ErrorCode.LANGUAGE_REQUEST_PARAMETER_MISSING
import com.procurement.mdm.infrastructure.web.dto.ErrorCode.LANGUAGE_REQUEST_PARAMETER_UNKNOWN
import org.hamcrest.core.IsEqual.equalTo
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
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
class AddressCountryControllerIT : AbstractRepositoryTest() {
    companion object {
        private const val COUNTRY = "md"
        private const val EMPTY_COUNTRY = "   "
        private const val INVALID_COUNTRY = "INVALID_COUNTRY"

        private const val LANGUAGE = "ro"
        private const val EMPTY_LANGUAGE = "   "
        private const val INVALID_LANGUAGE = "invalid"
        private const val UNKNOWN_LANGUAGE = "ul"

        private val COUNTRY_IDENTIFIER_FIRST = CountryIdentifier(
            scheme = "ISO",
            id = COUNTRY.toUpperCase(),
            description = "MOLDOVA",
            uri = "http://example.md"
        )

        private val COUNTRY_IDENTIFIER_SECOND = CountryIdentifier(
            scheme = "ISO",
            id = "BY",
            description = "BIELORUSIA",
            uri = "http://example.md"
        )
    }

    private lateinit var mockMvc: MockMvc
    private lateinit var addressCountryService: AddressCountryService

    @Autowired
    private lateinit var addressCountryRepository: AddressCountryRepository

    @Autowired
    private lateinit var advancedLanguageRepository: AdvancedLanguageRepository

    @Autowired
    private lateinit var countrySchemeRepository: CountrySchemeRepository

    @BeforeEach
    fun init(restDocumentation: RestDocumentationContextProvider) {
        addressCountryService = AddressCountryServiceImpl(
            addressCountryRepository = addressCountryRepository,
            advancedLanguageRepository = advancedLanguageRepository,
            countrySchemeRepository = countrySchemeRepository
        )

        val controller = AddressCountryController(addressCountryService)
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
    fun `Getting all of the countries is successful`() {
        initData()

        val url = getUrl()
        mockMvc.perform(
            get(url)
                .param("lang", LANGUAGE)
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType("application/json;charset=UTF-8"))
            .andExpect(jsonPath("$.data.length()", equalTo(2)))
            .andExpect(jsonPath("$.data[0].scheme", equalTo(COUNTRY_IDENTIFIER_FIRST.scheme)))
            .andExpect(jsonPath("$.data[0].id", equalTo(COUNTRY_IDENTIFIER_FIRST.id)))
            .andExpect(jsonPath("$.data[0].description", equalTo(COUNTRY_IDENTIFIER_FIRST.description)))
            .andExpect(jsonPath("$.data[0].uri", equalTo(COUNTRY_IDENTIFIER_FIRST.uri)))
            .andExpect(jsonPath("$.data[1].scheme", equalTo(COUNTRY_IDENTIFIER_SECOND.scheme)))
            .andExpect(jsonPath("$.data[1].id", equalTo(COUNTRY_IDENTIFIER_SECOND.id)))
            .andExpect(jsonPath("$.data[1].description", equalTo(COUNTRY_IDENTIFIER_SECOND.description)))
            .andExpect(jsonPath("$.data[1].uri", equalTo(COUNTRY_IDENTIFIER_SECOND.uri)))
            .andDo(
                document(
                    "address/country/get_all/success",
                    responseFields(ModelDescription.Address.Country.collection())
                )
            )
    }

    @Test
    fun `Getting all of the countries is error (language request parameter is missing)`() {
        initData()

        val url = getUrl()
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
            .andDo(
                document(
                    "address/country/get_all/errors/no_lang_query_param",
                    responseFields(ModelDescription.responseError())
                )
            )
    }

    @Test
    fun `Getting all of the countries is error (language request parameter is empty)`() {
        initData()

        val url = getUrl()
        mockMvc.perform(
            get(url)
                .param("lang", EMPTY_LANGUAGE)
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
            .andDo(
                document(
                    "address/country/get_all/errors/empty_lang",
                    responseFields(ModelDescription.responseError())
                )
            )
    }

    @Test
    fun `Getting all of the countries is error (language request parameter is invalid)`() {
        initData()

        val url = getUrl()
        mockMvc.perform(
            get(url)
                .param("lang", INVALID_LANGUAGE)
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
            .andDo(
                document(
                    "address/country/get_all/errors/invalid_lang",
                    responseFields(ModelDescription.responseError())
                )
            )
    }

    @Test
    fun `Getting all of the countries is error (language request parameter is unknown)`() {
        initData()

        val url = getUrl()
        mockMvc.perform(
            get(url)
                .param("lang", UNKNOWN_LANGUAGE)
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
            .andDo(
                document(
                    "address/country/get_all/errors/unknown_lang",
                    responseFields(ModelDescription.responseError())
                )
            )
    }

    @Test
    fun `Getting all of the countries is error (countries are not found)`() {
        initLanguages()

        val url = getUrl()
        mockMvc.perform(
            get(url)
                .param("lang", LANGUAGE)
        )
            .andExpect(status().isNotFound)
            .andExpect(content().contentType("application/json;charset=UTF-8"))
            .andExpect(jsonPath("$.errors.length()", equalTo(1)))
            .andExpect(jsonPath("$.errors[0].code", equalTo(COUNTRY_NOT_FOUND.code)))
            .andExpect(
                jsonPath(
                    "$.errors[0].description",
                    equalTo("The countries by language '$LANGUAGE' not found.")
                )
            )
            .andDo(
                document(
                    "address/country/get_all/errors/country_not_found",
                    responseFields(ModelDescription.responseError())
                )
            )
    }

    @Test
    fun `Getting the country by code is successful`() {
        initData()

        val url = getUrl(country = COUNTRY)
        mockMvc.perform(
            get(url)
                .param("lang", LANGUAGE)
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType("application/json;charset=UTF-8"))
            .andExpect(jsonPath("$.data.scheme", equalTo(COUNTRY_IDENTIFIER_FIRST.scheme)))
            .andExpect(jsonPath("$.data.id", equalTo(COUNTRY_IDENTIFIER_FIRST.id)))
            .andExpect(jsonPath("$.data.description", equalTo(COUNTRY_IDENTIFIER_FIRST.description)))
            .andExpect(jsonPath("$.data.uri", equalTo(COUNTRY_IDENTIFIER_FIRST.uri)))
            .andDo(
                document(
                    "address/country/get_by_code/success",
                    responseFields(ModelDescription.Address.Country.one())
                )
            )
    }

    @Test
    fun `Getting the country by code is error (language request parameter is missing)`() {
        initData()

        val url = getUrl(country = COUNTRY)
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
            .andDo(
                document(
                    "address/country/get_by_code/errors/no_lang_query_param",
                    responseFields(ModelDescription.responseError())
                )
            )
    }

    @Test
    fun `Getting the country by code is error (language request parameter is empty)`() {
        initData()

        val url = getUrl(country = COUNTRY)
        mockMvc.perform(
            get(url)
                .param("lang", EMPTY_LANGUAGE)
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
            .andDo(
                document(
                    "address/country/get_all/errors/empty_lang",
                    responseFields(ModelDescription.responseError())
                )
            )
    }

    @Test
    fun `Getting the country by code is error (language request parameter is invalid)`() {
        initData()

        val url = getUrl(country = COUNTRY)
        mockMvc.perform(
            get(url)
                .param("lang", INVALID_LANGUAGE)
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
            .andDo(
                document(
                    "address/country/get_all/errors/invalid_lang",
                    responseFields(ModelDescription.responseError())
                )
            )
    }

    @Test
    fun `Getting the country by code is error (language request parameter is unknown)`() {
        initData()

        val url = getUrl(country = COUNTRY)
        mockMvc.perform(
            get(url)
                .param("lang", UNKNOWN_LANGUAGE)
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
            .andDo(
                document(
                    "address/country/get_by_code/errors/unknown_lang",
                    responseFields(ModelDescription.responseError())
                )
            )
    }

    @Test
    fun `Getting the country by code is error (country code is empty)`() {
        initData()

        val url = getUrl(country = EMPTY_COUNTRY)
        mockMvc.perform(
            get(url)
                .param("lang", LANGUAGE)
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
                    "address/country/get_by_code/errors/empty_country",
                    responseFields(ModelDescription.responseError())
                )
            )
    }

    @Test
    fun `Getting the country by code is error (country code is invalid)`() {
        initData()

        val url = getUrl(country = INVALID_COUNTRY)
        mockMvc.perform(
            get(url)
                .param("lang", LANGUAGE)
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
                    "address/country/get_by_code/errors/invalid_country",
                    responseFields(ModelDescription.responseError())
                )
            )
    }

    @Test
    fun `Getting the country by code is error (country by code is not found)`() {
        initLanguages()

        val url = getUrl(country = COUNTRY)
        mockMvc.perform(
            get(url)
                .param("lang", LANGUAGE)
        )
            .andExpect(status().isNotFound)
            .andExpect(content().contentType("application/json;charset=UTF-8"))
            .andExpect(jsonPath("$.errors.length()", equalTo(1)))
            .andExpect(jsonPath("$.errors[0].code", equalTo(COUNTRY_NOT_FOUND.code)))
            .andExpect(
                jsonPath(
                    "$.errors[0].description",
                    equalTo("The country by code '$COUNTRY' and language '$LANGUAGE' not found.")
                )
            )
            .andDo(
                document(
                    "address/country/get_by_code/errors/country_not_found",
                    responseFields(ModelDescription.responseError())
                )
            )
    }

    private fun initData() {
        initLanguages()

        val sqlSchemes = loadSql("sql/list_schemes_init_data.sql")
        executeSQLScript(sqlSchemes)

        val sqlCountries = loadSql("sql/address/countries_init_data.sql")
        executeSQLScript(sqlCountries)
    }

    private fun initLanguages() {
        val sqlLanguages = loadSql("sql/languages_init_data.sql")
        executeSQLScript(sqlLanguages)
    }

    private fun getUrl(country: String? = null): String =
        if (country == null)
            "/addresses/countries"
        else
            String.format("/addresses/countries/%s", country)
}
