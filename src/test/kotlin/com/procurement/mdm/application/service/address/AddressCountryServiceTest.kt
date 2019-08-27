package com.procurement.mdm.application.service.address

import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import com.procurement.mdm.application.exception.CountryNotFoundException
import com.procurement.mdm.domain.entity.CountryEntity
import com.procurement.mdm.domain.exception.LanguageUnknownException
import com.procurement.mdm.domain.model.code.CountryCode
import com.procurement.mdm.domain.model.code.LanguageCode
import com.procurement.mdm.domain.model.identifier.CountryIdentifier
import com.procurement.mdm.domain.repository.AdvancedLanguageRepository
import com.procurement.mdm.domain.repository.address.AddressCountryRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class AddressCountryServiceTest {

    companion object {
        private const val COUNTRY = "md"
        private val COUNTRY_CODE = CountryCode(COUNTRY)
        private const val LANGUAGE = "ro"
        private val LANGUAGE_CODE = LanguageCode(LANGUAGE)

        private val COUNTRY_ENTITY = CountryEntity(
            scheme = "scheme-1",
            id = "id-1",
            description = "description-1",
            uri = "https://example-1.com"
        )

        private val COUNTRY_IDENTIFIER = CountryIdentifier(
            scheme = COUNTRY_ENTITY.scheme,
            id = COUNTRY_ENTITY.id,
            description = COUNTRY_ENTITY.description,
            uri = COUNTRY_ENTITY.uri
        )
    }

    private lateinit var addressCountryRepository: AddressCountryRepository
    private lateinit var advancedLanguageRepository: AdvancedLanguageRepository
    private lateinit var service: AddressCountryService

    @BeforeEach
    fun init() {
        addressCountryRepository = mock()
        advancedLanguageRepository = mock()

        service = AddressCountryServiceImpl(addressCountryRepository, advancedLanguageRepository)
    }

    @Test
    fun `Getting all of the countries is successful`() {
        whenever(advancedLanguageRepository.exists(eq(LANGUAGE_CODE)))
            .thenReturn(true)
        val countriesEntity = listOf(
            CountryEntity(
                scheme = "scheme-1",
                id = "id-1",
                description = "description-1",
                uri = "https://example-1.com"
            ),
            CountryEntity(
                scheme = "scheme-2",
                id = "id-2",
                description = "description-2",
                uri = "https://example-2.com"
            )
        )

        whenever(addressCountryRepository.findAll(eq(LANGUAGE_CODE)))
            .thenReturn(countriesEntity)

        val result = service.getAll(language = LANGUAGE)

        assertEquals(result.size, 2)
        assertEquals(
            result[0], CountryIdentifier(
                scheme = countriesEntity[0].scheme,
                id = countriesEntity[0].id,
                description = countriesEntity[0].description,
                uri = countriesEntity[0].uri
            )
        )
        assertEquals(
            result[1], CountryIdentifier(
                scheme = countriesEntity[1].scheme,
                id = countriesEntity[1].id,
                description = countriesEntity[1].description,
                uri = countriesEntity[1].uri
            )
        )
    }

    @Test
    fun `Get all countries is error (language request parameter is unknown)`() {
        whenever(advancedLanguageRepository.exists(eq(LANGUAGE_CODE)))
            .thenReturn(false)
        val exception = assertThrows<LanguageUnknownException> {
            service.getAll(language = LANGUAGE)
        }
        assertEquals("The unknown code of a language '$LANGUAGE'.", exception.description)
    }

    @Test
    fun `Get all countries is error (country is not found)`() {
        whenever(advancedLanguageRepository.exists(eq(LANGUAGE_CODE)))
            .thenReturn(true)
        whenever(addressCountryRepository.findAll(language = eq(LANGUAGE_CODE)))
            .thenReturn(emptyList())

        val exception = assertThrows<CountryNotFoundException> {
            service.getAll(language = LANGUAGE)
        }

        assertEquals(
            "The countries by language '$LANGUAGE' not found.",
            exception.description
        )
    }

    @Test
    fun `Getting the country by code is successful`() {
        whenever(advancedLanguageRepository.exists(eq(LANGUAGE_CODE)))
            .thenReturn(true)
        whenever(addressCountryRepository.findBy(country = eq(COUNTRY_CODE), language = eq(LANGUAGE_CODE)))
            .thenReturn(COUNTRY_ENTITY)

        val result = service.getBy(country = COUNTRY, language = LANGUAGE)

        assertEquals(COUNTRY_IDENTIFIER, result)
    }

    @Test
    fun `Get country by code is error (language request parameter is unknown)`() {
        whenever(advancedLanguageRepository.exists(eq(LANGUAGE_CODE)))
            .thenReturn(false)

        val exception = assertThrows<LanguageUnknownException> {
            service.getBy(country = COUNTRY, language = LANGUAGE)
        }

        assertEquals("The unknown code of a language '$LANGUAGE'.", exception.description)
    }

    @Test
    fun `Get country by code is error (country is not found)`() {
        whenever(advancedLanguageRepository.exists(eq(LANGUAGE_CODE)))
            .thenReturn(true)
        whenever(addressCountryRepository.findBy(country = eq(COUNTRY_CODE), language = eq(LANGUAGE_CODE)))
            .thenReturn(null)

        val exception = assertThrows<CountryNotFoundException> {
            service.getBy(country = COUNTRY, language = LANGUAGE)
        }

        assertEquals(
            "The country by code '$COUNTRY' and language '$LANGUAGE' not found.",
            exception.description
        )
    }
}
