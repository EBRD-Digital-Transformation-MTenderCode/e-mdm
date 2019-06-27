package com.procurement.mdm.domain.model.code

import com.procurement.mdm.domain.exception.InvalidCountryCodeException
import nl.jqno.equalsverifier.EqualsVerifier
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class CountryCodeTest {

    @Test
    fun `A passed parameter is correct`() {
        val countryCode = CountryCode(" md ")
        assertEquals("md", countryCode.value)
    }

    @Test
    fun `A passed parameter is empty`() {
        val exception = assertThrows<InvalidCountryCodeException> {
            CountryCode("")
        }

        assertEquals("Invalid country code (value is blank).", exception.description)
    }

    @Test
    fun `A passed parameter is blank`() {
        val exception = assertThrows<InvalidCountryCodeException> {
            CountryCode("   ")
        }

        assertEquals("Invalid country code (value is blank).", exception.description)
    }

    @Test
    fun `A passed parameter has a more length`() {
        val exception = assertThrows<InvalidCountryCodeException> {
            CountryCode("abc")
        }

        assertEquals(
            "Invalid country code: 'abc' (wrong length: '3' required: '2').",
            exception.description
        )
    }

    @Test
    fun `A passed parameter has a less length`() {
        val exception = assertThrows<InvalidCountryCodeException> {
            CountryCode("a")
        }

        assertEquals(
            "Invalid country code: 'a' (wrong length: '1' required: '2').",
            exception.description
        )
    }

    @Test
    fun `Test equals contract`() {
        EqualsVerifier.forClass(CountryCode::class.java)
            .verify()
    }

    @Test
    fun `Test toString`() {
        val countryCode = CountryCode(" md ")
        assertEquals("md", countryCode.toString())
    }
}
