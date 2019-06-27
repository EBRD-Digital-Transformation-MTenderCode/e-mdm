package com.procurement.mdm.infrastructure.repository.organization

import com.procurement.mdm.domain.model.code.CountryCode
import com.procurement.mdm.domain.repository.organization.OrganizationSchemeRepository
import com.procurement.mdm.infrastructure.repository.AbstractRepositoryTest
import com.procurement.mdm.infrastructure.repository.loadSql
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class OrganizationSchemeRepositoryIT : AbstractRepositoryTest() {
    companion object {
        private val COUNTRY_CODE = CountryCode("md")
        private val UNKNOWN_COUNTRY_CODE = CountryCode("uc")
    }

    @Autowired
    private lateinit var repository: OrganizationSchemeRepository

    private fun initData() {
        val sqlLanguages = loadSql("sql/languages_init_data.sql")
        executeSQLScript(sqlLanguages)

        val sqlSchemes = loadSql("sql/list_schemes_init_data.sql")
        executeSQLScript(sqlSchemes)

        val sqlCountries = loadSql("sql/organization/schemes_init_data.sql")
        executeSQLScript(sqlCountries)
    }

    @Test
    fun `Finding all of the organization schemes is successful`() {
        initData()

        val expect = setOf("ISO", "CUATM")
        val actual = repository.findAllOnlyCode(country = COUNTRY_CODE)

        assertEquals(2, actual.size)
        assertEquals(expect, actual.toSet())
    }

    @Test
    fun `Finding all of the organization schemes is error (unknown country)`() {
        initData()

        val schemesCodes = repository.findAllOnlyCode(country = UNKNOWN_COUNTRY_CODE)

        assertTrue(schemesCodes.isEmpty())
    }

    @Test
    fun `Finding all of the organization schemes is error (database is empty)`() {
        val schemesCodes = repository.findAllOnlyCode(country = COUNTRY_CODE)

        assertTrue(schemesCodes.isEmpty())
    }
}
