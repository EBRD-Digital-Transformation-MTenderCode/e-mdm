package com.procurement.mdm.domain.model.code

import com.procurement.mdm.domain.exception.InvalidRegionCodeException
import nl.jqno.equalsverifier.EqualsVerifier
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class RegionCodeTest {

    @Test
    fun `A passed parameter is correct`() {
        val regionCode = RegionCode(" abc-123456 ")
        assertEquals("abc-123456", regionCode.value)
    }

    @Test
    fun `A passed parameter is empty`() {
        val exception = assertThrows<InvalidRegionCodeException> {
            RegionCode("")
        }

        assertEquals("Invalid region code (value is blank).", exception.description)
    }

    @Test
    fun `A passed parameter is blank`() {
        val exception = assertThrows<InvalidRegionCodeException> {
            RegionCode("   ")
        }

        assertEquals("Invalid region code (value is blank).", exception.description)
    }

    @Test
    fun `Test equals contract`() {
        EqualsVerifier.forClass(RegionCode::class.java)
            .verify()
    }

    @Test
    fun `Test toString`() {
        val regionCode = RegionCode(" abc-123456 ")
        assertEquals("abc-123456", regionCode.toString())
    }
}
