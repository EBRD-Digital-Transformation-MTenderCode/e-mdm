package com.procurement.mdm.infrastructure.repository.scheme

import com.procurement.mdm.domain.entity.CountryEntity
import com.procurement.mdm.domain.model.code.CountryCode
import com.procurement.mdm.domain.model.code.LanguageCode
import com.procurement.mdm.domain.model.scheme.CountryScheme
import com.procurement.mdm.domain.repository.scheme.CountrySchemeRepository
import com.procurement.mdm.infrastructure.repository.AbstractRepositoryTest
import com.procurement.mdm.infrastructure.repository.loadSql
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class CountrySchemeRepositoryIT : AbstractRepositoryTest() {
    companion object {
        private val LANGUAGE_CODE = LanguageCode("ro")
        private val UNKNOWN_LANGUAGE_CODE = LanguageCode("ul")

        private val COUNTRY_CODE = CountryCode("md")
        private val UNKNOWN_COUNTRY_CODE = CountryCode("ua")
        private val COUNTRY_WITH_NO_DESCRIPTION = CountryCode("fr")

        private val COUNTRY_SCHEME = CountryScheme("ISO")
        private val UNKNOWN_COUNTRY_SCHEME = CountryScheme("CUATM")
        private const val DESCRIPTION = "MOLDOVA"
        private const val URI = "http://example.md"
    }

    @Autowired
    private lateinit var repository: CountrySchemeRepository

    private fun initData() {
        val sqlLanguages = loadSql("sql/languages_init_data.sql")
        executeSQLScript(sqlLanguages)

        val sqlSchemes = loadSql("sql/list_schemes_init_data.sql")
        executeSQLScript(sqlSchemes)

        val sqlCountrySchemes = loadSql("sql/scheme/country_schemes_init_data.sql")
        executeSQLScript(sqlCountrySchemes)
    }

    @Test
    fun `Scheme exists`() {
        initData()
        assertTrue(repository.existsBy(scheme = COUNTRY_SCHEME))
    }

    @Test
    fun `Scheme does not exist`() {
        initData()
        assertFalse(repository.existsBy(scheme = UNKNOWN_COUNTRY_SCHEME))
    }

    @Test
    fun `Scheme does not exist (database is empty)`() {
        assertFalse(repository.existsBy(scheme = COUNTRY_SCHEME))
    }

    @Test
    fun `Country by scheme exists`() {
        initData()
        assertTrue(repository.existsBy(scheme = COUNTRY_SCHEME, country = COUNTRY_CODE))
    }

    @Test
    fun `Country by scheme does not exist (unknown scheme)`() {
        initData()
        assertFalse(repository.existsBy(scheme = UNKNOWN_COUNTRY_SCHEME, country = COUNTRY_CODE))
    }

    @Test
    fun `Country by scheme does not exist (unknown country code)`() {
        initData()
        assertFalse(repository.existsBy(scheme = COUNTRY_SCHEME, country = UNKNOWN_COUNTRY_CODE))
    }

    @Test
    fun `Country by scheme does not exist (database is empty)`() {
        assertFalse(repository.existsBy(scheme = COUNTRY_SCHEME, country = COUNTRY_CODE))
    }

    @Test
    fun `Finding country by scheme is successful`() {
        initData()

        val actual = repository.findBy(country = COUNTRY_CODE, scheme = COUNTRY_SCHEME, language = LANGUAGE_CODE)

        val expected = CountryEntity(
            scheme = COUNTRY_SCHEME.value,
            id = COUNTRY_CODE.value.toUpperCase(),
            description = DESCRIPTION,
            uri = URI
        )

        assertEquals(expected, actual)
    }

    @Test
    fun `Finding country by scheme fails (unknown language)`() {
        initData()

        val actual = repository.findBy(
            country = COUNTRY_CODE,
            scheme = COUNTRY_SCHEME,
            language = UNKNOWN_LANGUAGE_CODE
        )

        assertTrue(actual == null)
    }

    @Test
    fun `Finding country by scheme fails (unknown country)`() {
        initData()

        val actual = repository.findBy(
            country = UNKNOWN_COUNTRY_CODE,
            scheme = COUNTRY_SCHEME,
            language = LANGUAGE_CODE
        )

        assertTrue(actual == null)
    }

    @Test
    fun `Finding country by scheme fails (unknown scheme)`() {
        initData()

        val actual = repository.findBy(
            country = COUNTRY_CODE,
            scheme = UNKNOWN_COUNTRY_SCHEME,
            language = LANGUAGE_CODE
        )

        assertTrue(actual == null)
    }

    @Test
    fun `Finding country by scheme fails (no description is found)`() {
        initData()

        val actual = repository.findBy(
            country = COUNTRY_WITH_NO_DESCRIPTION,
            scheme = COUNTRY_SCHEME,
            language = LANGUAGE_CODE
        )

        assertTrue(actual == null)
    }
}
