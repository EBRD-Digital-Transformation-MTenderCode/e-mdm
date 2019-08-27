package com.procurement.mdm.infrastructure.web.controller.address

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doThrow
import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.procurement.mdm.application.exception.CountryNotFoundException
import com.procurement.mdm.application.service.address.AddressCountryService
import com.procurement.mdm.domain.exception.InvalidCountryCodeException
import com.procurement.mdm.domain.exception.InvalidLanguageCodeException
import com.procurement.mdm.domain.exception.LanguageUnknownException
import com.procurement.mdm.domain.model.identifier.CountryIdentifier
import com.procurement.mdm.infrastructure.web.controller.RestExceptionHandler
import com.procurement.mdm.infrastructure.web.dto.ErrorCode.COUNTRY_NOT_FOUND
import com.procurement.mdm.infrastructure.web.dto.ErrorCode.INTERNAL_SERVER_ERROR
import com.procurement.mdm.infrastructure.web.dto.ErrorCode.INVALID_COUNTRY_CODE
import com.procurement.mdm.infrastructure.web.dto.ErrorCode.INVALID_LANGUAGE_CODE
import com.procurement.mdm.infrastructure.web.dto.ErrorCode.LANGUAGE_REQUEST_PARAMETER_MISSING
import com.procurement.mdm.infrastructure.web.dto.ErrorCode.LANGUAGE_REQUEST_PARAMETER_UNKNOWN
import org.hamcrest.core.IsEqual.equalTo
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders

class AddressCountryControllerTest {
    companion object {
        private const val COUNTRY = "md"
        private const val EMPTY_COUNTRY = "   "
        private const val INVALID_COUNTRY = "INVALID_COUNTRY"

        private const val LANGUAGE = "ro"
        private const val EMPTY_LANGUAGE = "   "
        private const val INVALID_LANGUAGE = "invalid"
        private const val UNKNOWN_LANGUAGE = "ul"

        private val COUNTRY_IDENTIFIER_FIRST = CountryIdentifier(
            scheme = "scheme-1",
            id = COUNTRY,
            description = "description-1",
            uri = "https://example-1.com"
        )

        private val COUNTRY_IDENTIFIER_SECOND = CountryIdentifier(
            scheme = "scheme-2",
            id = "id-2",
            description = "description-2",
            uri = "https://example-2.com"
        )
    }

    private lateinit var mockMvc: MockMvc
    private lateinit var addressCountryService: AddressCountryService

    @BeforeEach
    fun init() {
        addressCountryService = mock()

        val controller = AddressCountryController(addressCountryService)
        val restExceptionHandler = RestExceptionHandler()
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
            .setControllerAdvice(restExceptionHandler)
            .build()
    }

    @Test
    fun `Getting all of the countries is successful`() {
        whenever(addressCountryService.getAll(language = eq(LANGUAGE)))
            .thenReturn(
                listOf(COUNTRY_IDENTIFIER_FIRST, COUNTRY_IDENTIFIER_SECOND)
            )

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

        verify(addressCountryService, times(1))
            .getAll(language = any())
    }

    @Test
    fun `Getting all of the countries is error (language request parameter is missing)`() {
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

        verify(addressCountryService, times(0))
            .getAll(language = any())
    }

    @Test
    fun `Getting all of the countries is error (language request parameter is empty)`() {

        doThrow(InvalidLanguageCodeException(description = "Invalid language code (value is blank)."))
            .whenever(addressCountryService)
            .getAll(language = eq(EMPTY_LANGUAGE))

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

        verify(addressCountryService, times(1))
            .getAll(language = any())
    }

    @Test
    fun `Getting all of the countries is error (language request parameter is invalid)`() {

        doThrow(InvalidLanguageCodeException(description = "Invalid language code: '$INVALID_LANGUAGE' (wrong length: '${INVALID_LANGUAGE.length}' required: '2')."))
            .whenever(addressCountryService)
            .getAll(language = eq(INVALID_LANGUAGE))

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

        verify(addressCountryService, times(1))
            .getAll(language = any())
    }

    @Test
    fun `Getting all of the countries is error (language request parameter is unknown)`() {
        doThrow(LanguageUnknownException(language = UNKNOWN_LANGUAGE))
            .whenever(addressCountryService)
            .getAll(language = eq(UNKNOWN_LANGUAGE))

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

        verify(addressCountryService, times(1))
            .getAll(language = any())
    }

    @Test
    fun `Getting all of the countries is error (countries are not found)`() {
        doThrow(CountryNotFoundException(language = LANGUAGE))
            .whenever(addressCountryService)
            .getAll(language = eq(LANGUAGE))

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

        verify(addressCountryService, times(1))
            .getAll(language = any())
    }

    @Test
    fun `Getting all of the countries is error (internal server error)`() {
        doThrow(RuntimeException())
            .whenever(addressCountryService)
            .getAll(language = eq(LANGUAGE))

        val url = getUrl()
        mockMvc.perform(
            get(url)
                .param("lang", LANGUAGE)
        )
            .andExpect(status().isInternalServerError)
            .andExpect(content().contentType("application/json;charset=UTF-8"))
            .andExpect(jsonPath("$.errors.length()", equalTo(1)))
            .andExpect(jsonPath("$.errors[0].code", equalTo(INTERNAL_SERVER_ERROR.code)))
            .andExpect(jsonPath("$.errors[0].description", equalTo("Internal server error.")))

        verify(addressCountryService, times(1))
            .getAll(language = any())
    }

    @Test
    fun `Getting the country by code is successful`() {
        whenever(addressCountryService.getBy(country = eq(COUNTRY), language = eq(LANGUAGE)))
            .thenReturn(COUNTRY_IDENTIFIER_FIRST)

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

        verify(addressCountryService, times(1))
            .getBy(country = any(), language = any())
    }

    @Test
    fun `Getting the country by code is error (language request parameter is missing)`() {
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

        verify(addressCountryService, times(0))
            .getBy(country = any(), language = any())
    }

    @Test
    fun `Getting the country by code is error (language request parameter is empty)`() {
        doThrow(InvalidLanguageCodeException(description = "Invalid language code (value is blank)."))
            .whenever(addressCountryService)
            .getBy(country = eq(COUNTRY), language = eq(EMPTY_LANGUAGE))

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

        verify(addressCountryService, times(1))
            .getBy(country = any(), language = any())
    }

    @Test
    fun `Getting the country by code is error (language request parameter is invalid)`() {

        doThrow(InvalidLanguageCodeException(description = "Invalid language code: '$INVALID_LANGUAGE' (wrong length: '${INVALID_LANGUAGE.length}' required: '2')."))
            .whenever(addressCountryService)
            .getBy(country = eq(COUNTRY), language = eq(INVALID_LANGUAGE))

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

        verify(addressCountryService, times(1))
            .getBy(country = any(), language = any())
    }

    @Test
    fun `Getting the country by code is error (language request parameter is unknown)`() {
        doThrow(LanguageUnknownException(language = UNKNOWN_LANGUAGE))
            .whenever(addressCountryService)
            .getBy(country = eq(COUNTRY), language = eq(UNKNOWN_LANGUAGE))

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

        verify(addressCountryService, times(1))
            .getBy(country = any(), language = any())
    }

    @Test
    fun `Getting the country by code is error (country code is empty)`() {
        doThrow(InvalidCountryCodeException(description = "Invalid country code (value is blank)."))
            .whenever(addressCountryService)
            .getBy(country = eq(EMPTY_COUNTRY), language = eq(LANGUAGE))

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

        verify(addressCountryService, times(1))
            .getBy(country = any(), language = any())
    }

    @Test
    fun `Getting the country by code is error (country code is invalid)`() {

        doThrow(InvalidCountryCodeException(description = "Invalid country code: '$INVALID_COUNTRY' (wrong length: '${INVALID_COUNTRY.length}' required: '2')."))
            .whenever(addressCountryService)
            .getBy(country = eq(INVALID_COUNTRY), language = eq(LANGUAGE))

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

        verify(addressCountryService, times(1))
            .getBy(country = any(), language = any())
    }

    @Test
    fun `Getting the country by code is error (country by code is not found)`() {
        doThrow(CountryNotFoundException(country = COUNTRY, language = LANGUAGE))
            .whenever(addressCountryService)
            .getBy(country = eq(COUNTRY), language = eq(LANGUAGE))

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

        verify(addressCountryService, times(1))
            .getBy(country = any(), language = any())
    }

    @Test
    fun `Getting the country by code is error (internal server error)`() {
        doThrow(RuntimeException())
            .whenever(addressCountryService)
            .getBy(country = eq(COUNTRY), language = eq(LANGUAGE))

        val url = getUrl(country = COUNTRY)
        mockMvc.perform(
            get(url)
                .param("lang", LANGUAGE)
        )
            .andExpect(status().isInternalServerError)
            .andExpect(content().contentType("application/json;charset=UTF-8"))
            .andExpect(jsonPath("$.errors.length()", equalTo(1)))
            .andExpect(jsonPath("$.errors[0].code", equalTo(INTERNAL_SERVER_ERROR.code)))
            .andExpect(jsonPath("$.errors[0].description", equalTo("Internal server error.")))

        verify(addressCountryService, times(1))
            .getBy(country = any(), language = any())
    }

    private fun getUrl(country: String? = null): String =
        if (country == null)
            "/addresses/countries"
        else
            String.format("/addresses/countries/%s", country)
}
