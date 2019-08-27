package com.procurement.mdm.domain.model.code

import com.procurement.mdm.domain.exception.InvalidLanguageCodeException
import nl.jqno.equalsverifier.EqualsVerifier
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class LanguageCodeTest {

    @Test
    fun `A passed parameter is correct`() {
        val languageCode = LanguageCode(" rO ")
        assertEquals("rO", languageCode.value)
    }

    @Test
    fun `A passed parameter is empty`() {
        val exception = assertThrows<InvalidLanguageCodeException> {
            LanguageCode("")
        }

        assertEquals("Invalid language code (value is blank).", exception.description)
    }

    @Test
    fun `A passed parameter is blank`() {
        val exception = assertThrows<InvalidLanguageCodeException> {
            LanguageCode("   ")
        }

        assertEquals("Invalid language code (value is blank).", exception.description)
    }

    @Test
    fun `A passed parameter has a more length`() {
        val exception = assertThrows<InvalidLanguageCodeException> {
            LanguageCode("abc")
        }

        assertEquals(
            "Invalid language code: 'abc' (wrong length: '3' required: '2').",
            exception.description
        )
    }

    @Test
    fun `A passed parameter has a less length`() {
        val exception = assertThrows<InvalidLanguageCodeException> {
            LanguageCode("a")
        }

        assertEquals(
            "Invalid language code: 'a' (wrong length: '1' required: '2').",
            exception.description
        )
    }

    @Test
    fun `Test equals contract`() {
        EqualsVerifier.forClass(LanguageCode::class.java)
            .verify()
    }

    @Test
    fun `Test toString`() {
        val languageCode = LanguageCode(" rO ")
        assertEquals("rO", languageCode.toString())
    }
}
