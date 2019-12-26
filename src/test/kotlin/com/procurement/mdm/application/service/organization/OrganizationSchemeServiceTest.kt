package com.procurement.mdm.application.service.organization

import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.reset
import com.nhaarman.mockito_kotlin.whenever
import com.procurement.mdm.application.exception.OrganizationSchemeNotFoundException
import com.procurement.mdm.domain.exception.CountryUnknownException
import com.procurement.mdm.domain.model.code.CountryCode
import com.procurement.mdm.domain.repository.address.AddressCountryRepository
import com.procurement.mdm.domain.repository.organization.OrganizationSchemeRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class OrganizationSchemeServiceTest {

    companion object {
        private const val COUNTRY = "md"
        private val COUNTRY_CODE = CountryCode(COUNTRY)

        private val SCHEMES_CODES = listOf("ISO", "CUATM")
    }

    private val organizationSchemeRepository = mock<OrganizationSchemeRepository>()
    private val addressCountryRepository = mock<AddressCountryRepository>()
    private val service: OrganizationSchemeService =
        OrganizationSchemeServiceImpl(organizationSchemeRepository, addressCountryRepository)

    @BeforeEach
    fun init() {
        reset(organizationSchemeRepository)
        reset(addressCountryRepository)
    }

    @Nested
    inner class SchemesByCountry {
        @Test
        fun `Getting the organization schemes is successful`() {
            whenever(addressCountryRepository.exists(code = eq(COUNTRY_CODE)))
                .thenReturn(true)
            whenever(organizationSchemeRepository.find(country = eq(COUNTRY_CODE)))
                .thenReturn(SCHEMES_CODES)

            val actual = service.find(country = COUNTRY)

            assertEquals(SCHEMES_CODES.toSet(), actual.toSet())
        }

        @Test
        fun `Getting the organization schemes is error (country request parameter is unknown)`() {
            whenever(addressCountryRepository.exists(code = eq(COUNTRY_CODE)))
                .thenReturn(false)

            val exception = assertThrows<CountryUnknownException> {
                service.find(country = COUNTRY)
            }

            assertEquals("The unknown code of a country '$COUNTRY'.", exception.description)
        }

        @Test
        fun `Getting the organization schemes is error (schemes codes by country is not found)`() {
            whenever(addressCountryRepository.exists(code = eq(COUNTRY_CODE)))
                .thenReturn(true)
            whenever(organizationSchemeRepository.find(country = eq(COUNTRY_CODE)))
                .thenReturn(emptyList())

            val exception = assertThrows<OrganizationSchemeNotFoundException> {
                service.find(country = COUNTRY)
            }

            assertEquals("The organization schemes for country '$COUNTRY' not found.", exception.description)
        }
    }

    @Nested
    inner class SchemesByCountries {
        @Test
        fun `Getting the organization schemes is successful`() {
            whenever(addressCountryRepository.exists(code = eq(COUNTRY_CODE)))
                .thenReturn(true)
            whenever(organizationSchemeRepository.find(country = eq(COUNTRY_CODE)))
                .thenReturn(SCHEMES_CODES)

            val actual = service.find(countries = listOf(COUNTRY))

            assertEquals(1, actual.size)
            assertEquals(SCHEMES_CODES.toSet(), actual[COUNTRY_CODE]!!.toSet())
        }

        @Test
        fun `Getting the organization schemes is error (country code is unknown)`() {
            whenever(addressCountryRepository.exists(code = eq(COUNTRY_CODE)))
                .thenReturn(false)

            val exception = assertThrows<CountryUnknownException> {
                service.find(countries = listOf(COUNTRY))
            }

            assertEquals("The unknown code of a country '$COUNTRY'.", exception.description)
        }

        @Test
        fun `Getting the organization schemes is error (schemes codes by country is not found)`() {
            whenever(addressCountryRepository.exists(code = eq(COUNTRY_CODE)))
                .thenReturn(true)
            whenever(organizationSchemeRepository.find(country = eq(COUNTRY_CODE)))
                .thenReturn(emptyList())

            val exception = assertThrows<OrganizationSchemeNotFoundException> {
                service.find(countries = listOf(COUNTRY))
            }

            assertEquals("The organization schemes for country '$COUNTRY' not found.", exception.description)
        }
    }
}
