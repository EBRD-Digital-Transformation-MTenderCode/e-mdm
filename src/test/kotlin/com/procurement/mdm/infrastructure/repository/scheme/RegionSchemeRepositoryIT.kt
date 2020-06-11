package com.procurement.mdm.infrastructure.repository.scheme

import com.procurement.mdm.domain.entity.RegionEntity
import com.procurement.mdm.domain.model.code.CountryCode
import com.procurement.mdm.domain.model.code.LanguageCode
import com.procurement.mdm.domain.model.code.RegionCode
import com.procurement.mdm.domain.model.scheme.RegionScheme
import com.procurement.mdm.domain.repository.scheme.RegionSchemeRepository
import com.procurement.mdm.infrastructure.repository.AbstractRepositoryTest
import com.procurement.mdm.infrastructure.repository.loadSql
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class RegionSchemeRepositoryIT : AbstractRepositoryTest() {
    companion object {
        private val LANGUAGE_CODE = LanguageCode("en")
        private val UNKNOWN_LANGUAGE_CODE = LanguageCode("ul")

        private val COUNTRY_CODE = CountryCode("md")
        private val UNKNOWN_COUNTRY_CODE = CountryCode("ua")

        private val REGION_CODE = RegionCode("0101000")
        private val UNKNOWN_REGION_CODE = RegionCode("0000000")
        private val REGION_WITH_NO_DESCRIPTION = RegionCode("0301000")

        private val REGION_SCHEME = RegionScheme("CUATM")
        private val UNKNOWN_REGION_SCHEME = RegionScheme("ISO")
        private const val DESCRIPTION = "mun.Chisinau"
        private const val URI = "http://statistica.md"
    }

    @Autowired
    private lateinit var repository: RegionSchemeRepository

    private fun initData() {
        val sqlLanguages = loadSql("sql/languages_init_data.sql")
        executeSQLScript(sqlLanguages)

        val sqlSchemes = loadSql("sql/list_schemes_init_data.sql")
        executeSQLScript(sqlSchemes)

        val sqlRegionSchemes = loadSql("sql/scheme/region_schemes_init_data.sql")
        executeSQLScript(sqlRegionSchemes)
    }

    @Test
    fun `Scheme exists`() {
        initData()
        assertTrue(repository.existsBy(scheme = REGION_SCHEME))
    }

    @Test
    fun `Scheme does not exist`() {
        initData()
        assertFalse(repository.existsBy(scheme = UNKNOWN_REGION_SCHEME))
    }

    @Test
    fun `Scheme does not exist (database is empty)`() {
        assertFalse(repository.existsBy(scheme = REGION_SCHEME))
    }

    @Test
    fun `Region by scheme exists`() {
        initData()
        assertTrue(repository.existsBy(scheme = REGION_SCHEME, region = REGION_CODE))
    }

    @Test
    fun `Region by scheme does not exist (unknown scheme)`() {
        initData()
        assertFalse(repository.existsBy(scheme = UNKNOWN_REGION_SCHEME, region = REGION_CODE))
    }

    @Test
    fun `Region by scheme does not exist (unknown region code)`() {
        initData()
        assertFalse(repository.existsBy(scheme = REGION_SCHEME, region = UNKNOWN_REGION_CODE))
    }

    @Test
    fun `Region by scheme does not exist (database is empty)`() {
        assertFalse(repository.existsBy(scheme = REGION_SCHEME, region = REGION_CODE))
    }

    @Test
    fun `Region by scheme and country exists`() {
        initData()
        assertTrue(repository.existsBy(scheme = REGION_SCHEME, region = REGION_CODE, country = COUNTRY_CODE))
    }

    @Test
    fun `Region by scheme and country does not exist (unknown scheme)`() {
        initData()
        assertFalse(repository.existsBy(scheme = UNKNOWN_REGION_SCHEME, region = REGION_CODE, country = COUNTRY_CODE))
    }

    @Test
    fun `Region by scheme and country does not exist (unknown region code)`() {
        initData()
        assertFalse(repository.existsBy(scheme = REGION_SCHEME, region = UNKNOWN_REGION_CODE, country = COUNTRY_CODE))
    }

    @Test
    fun `Region by scheme and country does not exist (unknown country code)`() {
        initData()
        assertFalse(repository.existsBy(scheme = REGION_SCHEME, region = REGION_CODE, country = UNKNOWN_COUNTRY_CODE))
    }

    @Test
    fun `Region by scheme and country does not exist (database is empty)`() {
        assertFalse(repository.existsBy(scheme = REGION_SCHEME, region = REGION_CODE, country = COUNTRY_CODE))
    }

    @Test
    fun `Finding region is successful`() {
        initData()

        val actual = repository.findBy(
            country = COUNTRY_CODE,
            scheme = REGION_SCHEME,
            language = LANGUAGE_CODE,
            region = REGION_CODE
        )

        val expected = RegionEntity(
            scheme = REGION_SCHEME.value.toUpperCase(),
            id = REGION_CODE.value.toUpperCase(),
            description = DESCRIPTION,
            uri = URI
        )

        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun `Finding region fails (unknown region)`() {
        initData()

        val actual = repository.findBy(
            country = COUNTRY_CODE,
            scheme = REGION_SCHEME,
            language = LANGUAGE_CODE,
            region = UNKNOWN_REGION_CODE
        )

        assertTrue(actual == null)
    }

    @Test
    fun `Finding region fails (unknown language)`() {
        initData()

        val actual = repository.findBy(
            country = COUNTRY_CODE,
            scheme = REGION_SCHEME,
            language = UNKNOWN_LANGUAGE_CODE,
            region = REGION_CODE
        )

        assertTrue(actual == null)
    }

    @Test
    fun `Finding region fails (unknown country)`() {
        initData()

        val actual = repository.findBy(
            country = UNKNOWN_COUNTRY_CODE,
            scheme = REGION_SCHEME,
            language = LANGUAGE_CODE,
            region = REGION_CODE
        )

        assertTrue(actual == null)
    }

    @Test
    fun `Finding region fails (unknown scheme)`() {
        initData()

        val actual = repository.findBy(
            country = COUNTRY_CODE,
            scheme = UNKNOWN_REGION_SCHEME,
            language = LANGUAGE_CODE,
            region = REGION_CODE
        )

        assertTrue(actual == null)
    }

    @Test
    fun `Finding region fails (no description is found)`() {
        initData()

        val actual = repository.findBy(
            country = COUNTRY_CODE,
            scheme = REGION_SCHEME,
            language = LANGUAGE_CODE,
            region = REGION_WITH_NO_DESCRIPTION
        )

        assertTrue(actual == null)
    }
}
