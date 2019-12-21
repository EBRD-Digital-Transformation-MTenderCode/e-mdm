package com.procurement.mdm.application.service.organization

import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import com.procurement.mdm.application.exception.OrganizationSchemeNotFoundException
import com.procurement.mdm.domain.exception.CountryUnknownException
import com.procurement.mdm.domain.model.code.CountryCode
import com.procurement.mdm.domain.repository.address.AddressCountryRepository
import com.procurement.mdm.domain.repository.organization.OrganizationSchemeRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class OrganizationSchemeServiceTest {

    companion object {
        private const val COUNTRY = "md"
        private val COUNTRY_CODE = CountryCode(COUNTRY)

        private val SCHEMES_CODES = listOf("ISO", "CUATM")
    }

    private lateinit var organizationSchemeRepository: OrganizationSchemeRepository
    private lateinit var addressCountryRepository: AddressCountryRepository
    private lateinit var service: OrganizationSchemeService

    @BeforeEach
    fun init() {
        organizationSchemeRepository = mock()
        addressCountryRepository = mock()

        service = OrganizationSchemeServiceImpl(organizationSchemeRepository, addressCountryRepository)
    }

    @Test
    @DisplayName("Getting the organization schemes is successful")
    fun test1() {
        whenever(addressCountryRepository.exists(code = eq(COUNTRY_CODE)))
            .thenReturn(true)
        whenever(organizationSchemeRepository.find(country = eq(COUNTRY_CODE)))
            .thenReturn(SCHEMES_CODES)

        val actual = service.findAllOnlyCode(country = COUNTRY)

        assertEquals(SCHEMES_CODES.toSet(), actual.toSet())
    }

    @Test
    fun `Getting the organization schemes is error (country request parameter is unknown)`() {
        whenever(addressCountryRepository.exists(code = eq(COUNTRY_CODE)))
            .thenReturn(false)

        val exception = assertThrows<CountryUnknownException> {
            service.findAllOnlyCode(country = COUNTRY)
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
            service.findAllOnlyCode(country = COUNTRY)
        }

        assertEquals("The organization schemes for country '$COUNTRY' not found.", exception.description)
    }
}
