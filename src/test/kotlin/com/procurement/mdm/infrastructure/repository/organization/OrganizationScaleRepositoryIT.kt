package com.procurement.mdm.infrastructure.repository.organization

import com.procurement.mdm.domain.model.code.CountryCode
import com.procurement.mdm.domain.repository.organization.OrganizationScaleRepository
import com.procurement.mdm.infrastructure.repository.AbstractRepositoryTest
import com.procurement.mdm.infrastructure.repository.loadSql
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class OrganizationScaleRepositoryIT : AbstractRepositoryTest() {
    companion object {
        private val COUNTRY_CODE = CountryCode("md")
        private val UNKNOWN_COUNTRY_CODE = CountryCode("uc")
    }

    @Autowired
    private lateinit var repository: OrganizationScaleRepository

    private fun initData() {
        val sqlCountries = loadSql("sql/organization/scale_init_data.sql")
        executeSQLScript(sqlCountries)
    }

    @Test
    fun `Finding all of the organization scales is successful`() {
        initData()

        val expect = setOf("MICRO", "SME")
        val actual = repository.findAllOnlyCode(country = COUNTRY_CODE)

        assertEquals(2, actual.size)
        assertEquals(expect, actual.toSet())
    }

    @Test
    fun `Finding all of the organization scales is error (unknown country)`() {
        initData()

        val scalesCodes = repository.findAllOnlyCode(country = UNKNOWN_COUNTRY_CODE)

        assertTrue(scalesCodes.isEmpty())
    }

    @Test
    fun `Finding all of the organization scales is error (database is empty)`() {
        val scalesCodes = repository.findAllOnlyCode(country = COUNTRY_CODE)

        assertTrue(scalesCodes.isEmpty())
    }
}
