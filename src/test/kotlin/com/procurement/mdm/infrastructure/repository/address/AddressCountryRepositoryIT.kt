package com.procurement.mdm.infrastructure.repository.address

import com.procurement.mdm.domain.entity.CountryEntity
import com.procurement.mdm.domain.model.code.CountryCode
import com.procurement.mdm.domain.model.code.LanguageCode
import com.procurement.mdm.domain.repository.address.AddressCountryRepository
import com.procurement.mdm.infrastructure.repository.AbstractRepositoryTest
import com.procurement.mdm.infrastructure.repository.loadSql
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class AddressCountryRepositoryIT : AbstractRepositoryTest() {
    companion object {
        private val LANGUAGE_CODE = LanguageCode("ro")
        private val UNKNOWN_LANGUAGE_CODE = LanguageCode("ul")

        private val COUNTRY_CODE = CountryCode("md")
        private val UNKNOWN_COUNTRY_CODE = CountryCode("uc")

        private const val SCHEME_CODE = "ISO"
        private const val DESCRIPTION = "MOLDOVA"
        private const val URI = "http://example.md"
    }

    @Autowired
    private lateinit var repository: AddressCountryRepository

    private fun initData() {
        val sqlLanguages = loadSql("sql/languages_init_data.sql")
        executeSQLScript(sqlLanguages)

        val sqlSchemes = loadSql("sql/list_schemes_init_data.sql")
        executeSQLScript(sqlSchemes)

        val sqlCountries = loadSql("sql/address/countries_init_data.sql")
        executeSQLScript(sqlCountries)
    }

    @Test
    fun `Country is exists`() {
        initData()
        assertTrue(repository.exists(code = COUNTRY_CODE))
    }

    @Test
    fun `Country is not exists`() {
        initData()
        assertFalse(repository.exists(code = UNKNOWN_COUNTRY_CODE))
    }

    @Test
    fun `Country is not exists (database is empty)`() {
        assertFalse(repository.exists(code = COUNTRY_CODE))
    }

    @Test
    fun `Finding all of the countries is successful`() {
        initData()

        val result = repository.findAll(language = LANGUAGE_CODE)

        assertEquals(2, result.size)
    }

    @Test
    fun `Finding all of the countries is error (countries are not found)`() {
        initData()

        val countries = repository.findAll(language = UNKNOWN_LANGUAGE_CODE)

        assertTrue(countries.isEmpty())
    }

    @Test
    fun `Finding all of the countries is error (database is empty)`() {
        val countries = repository.findAll(language = LANGUAGE_CODE)

        assertTrue(countries.isEmpty())
    }

    @Test
    fun `Finding country by code is successful`() {
        initData()

        val actual = repository.findBy(country = COUNTRY_CODE, language = LANGUAGE_CODE)

        val expected = CountryEntity(
            scheme = SCHEME_CODE,
            id = COUNTRY_CODE.value.toUpperCase(),
            description = DESCRIPTION,
            uri = URI
        )

        assertEquals(expected, actual)
    }

    @Test
    fun `Finding country by code is error (unknown id)`() {
        initData()

        val country = repository.findBy(country = UNKNOWN_COUNTRY_CODE, language = LANGUAGE_CODE)

        assertNull(country)
    }

    @Test
    fun `Finding country by code is error (unknown language)`() {
        initData()

        val country = repository.findBy(country = COUNTRY_CODE, language = UNKNOWN_LANGUAGE_CODE)

        assertNull(country)
    }

    @Test
    fun `Finding country by code is error (database is empty)`() {
        val country = repository.findBy(country = COUNTRY_CODE, language = LANGUAGE_CODE)

        assertNull(country)
    }
}
