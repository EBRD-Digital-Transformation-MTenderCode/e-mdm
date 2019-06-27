package com.procurement.mdm.application.service.organization

import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import com.procurement.mdm.application.exception.OrganizationScaleNotFoundException
import com.procurement.mdm.domain.exception.CountryUnknownException
import com.procurement.mdm.domain.model.code.CountryCode
import com.procurement.mdm.domain.repository.address.AddressCountryRepository
import com.procurement.mdm.domain.repository.organization.OrganizationScaleRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class OrganizationScaleServiceTest {

    companion object {
        private const val COUNTRY = "md"
        private val COUNTRY_CODE = CountryCode(COUNTRY)

        private val SCALES_CODES = listOf("MICRO", "LARGE")
    }

    private lateinit var organizationScaleRepository: OrganizationScaleRepository
    private lateinit var addressCountryRepository: AddressCountryRepository
    private lateinit var service: OrganizationScaleService

    @BeforeEach
    fun init() {
        organizationScaleRepository = mock()
        addressCountryRepository = mock()

        service = OrganizationScaleServiceImpl(organizationScaleRepository, addressCountryRepository)
    }

    @Test
    fun `Getting the organization scales is successful`() {
        whenever(addressCountryRepository.exists(code = eq(COUNTRY_CODE)))
            .thenReturn(true)
        whenever(organizationScaleRepository.findAllOnlyCode(country = eq(COUNTRY_CODE)))
            .thenReturn(SCALES_CODES)

        val actual = service.findAllOnlyCode(country = COUNTRY)

        assertEquals(SCALES_CODES.toSet(), actual.toSet())
    }

    @Test
    fun `Getting the organization scales is error (country request parameter is unknown)`() {
        whenever(addressCountryRepository.exists(code = eq(COUNTRY_CODE)))
            .thenReturn(false)

        val exception = assertThrows<CountryUnknownException> {
            service.findAllOnlyCode(country = COUNTRY)
        }

        assertEquals("The unknown code of a country '$COUNTRY'.", exception.description)
    }

    @Test
    fun `Getting the organization scales is error (scales codes by country is not found)`() {
        whenever(addressCountryRepository.exists(code = eq(COUNTRY_CODE)))
            .thenReturn(true)
        whenever(organizationScaleRepository.findAllOnlyCode(country = eq(COUNTRY_CODE)))
            .thenReturn(emptyList())

        val exception = assertThrows<OrganizationScaleNotFoundException> {
            service.findAllOnlyCode(country = COUNTRY)
        }

        assertEquals("The organization scale for country '$COUNTRY' not found.", exception.description)
    }
}
