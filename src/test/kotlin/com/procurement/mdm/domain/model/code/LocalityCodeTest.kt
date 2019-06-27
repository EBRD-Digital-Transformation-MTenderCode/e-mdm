package com.procurement.mdm.domain.model.code

import com.procurement.mdm.domain.exception.InvalidLocalityCodeException
import nl.jqno.equalsverifier.EqualsVerifier
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class LocalityCodeTest {

    @Test
    fun `A passed parameter is correct`() {
        val localityCode = LocalityCode(" abc-123456 ")
        assertEquals("abc-123456", localityCode.value)
    }

    @Test
    fun `A passed parameter is empty`() {
        val exception = assertThrows<InvalidLocalityCodeException> {
            LocalityCode("")
        }

        assertEquals("Invalid locality code (value is blank).", exception.description)
    }

    @Test
    fun `A passed parameter is blank`() {
        val exception = assertThrows<InvalidLocalityCodeException> {
            LocalityCode("   ")
        }

        assertEquals("Invalid locality code (value is blank).", exception.description)
    }

    @Test
    fun `Test equals contract`() {
        EqualsVerifier.forClass(LocalityCode::class.java)
            .verify()
    }

    @Test
    fun `Test toString`() {
        val localityCode = LocalityCode(" abc-123456 ")
        assertEquals("abc-123456", localityCode.toString())
    }
}
