package com.procurement.mdm.infrastructure.repository.scheme

import com.procurement.mdm.domain.entity.LocalityEntity
import com.procurement.mdm.domain.model.code.LanguageCode
import com.procurement.mdm.domain.model.code.LocalityCode
import com.procurement.mdm.domain.model.code.RegionCode
import com.procurement.mdm.domain.model.scheme.LocalityScheme
import com.procurement.mdm.domain.repository.scheme.LocalitySchemeRepository
import com.procurement.mdm.infrastructure.repository.AbstractRepositoryTest
import com.procurement.mdm.infrastructure.repository.loadSql
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class LocalitySchemeRepositoryIT : AbstractRepositoryTest() {
    companion object {
        private val LANGUAGE_CODE = LanguageCode("en")
        private val UNKNOWN_LANGUAGE_CODE = LanguageCode("ul")

        private val REGION_CODE = RegionCode("REGION-1")
        private val UNKNOWN_REGION_CODE = RegionCode("0000000")

        private val LOCALITY_SCHEME = LocalityScheme("CUATM")
        private val UNKNOWN_LOCALITY_SCHEME = LocalityScheme("ISO")

        private val LOCALITY_CODE = LocalityCode("LOCALITY-1")
        private val UNKNOWN_LOCALITY_CODE = LocalityCode("unknown-locality")
        private val LOCALITY_WITH_NO_DESCRIPTION = LocalityCode("LOCALITY-2")

        private const val DESCRIPTION = "mun.Chisinau"
        private const val URI = "http://statistica.md"
    }

    @Autowired
    private lateinit var repository: LocalitySchemeRepository

    private fun initData() {
        val sqlLanguages = loadSql("sql/languages_init_data.sql")
        executeSQLScript(sqlLanguages)

        val sqlSchemes = loadSql("sql/list_schemes_init_data.sql")
        executeSQLScript(sqlSchemes)

        val sqlLocalitySchemes = loadSql("sql/scheme/locality_schemes_init_data.sql")
        executeSQLScript(sqlLocalitySchemes)
    }

    @Test
    fun `Scheme exists`() {
        initData()
        assertTrue(repository.existsBy(scheme = LOCALITY_SCHEME))
    }

    @Test
    fun `Scheme does not exist`() {
        initData()
        assertFalse(repository.existsBy(scheme = UNKNOWN_LOCALITY_SCHEME))
    }

    @Test
    fun `Scheme does not exist (database is empty)`() {
        assertFalse(repository.existsBy(scheme = LOCALITY_SCHEME))
    }

    @Test
    fun `Locality by scheme exists`() {
        initData()
        assertTrue(repository.existsBy(scheme = LOCALITY_SCHEME, locality = LOCALITY_CODE))
    }

    @Test
    fun `Locality by scheme does not exist (unknown scheme)`() {
        initData()
        assertFalse(repository.existsBy(scheme = UNKNOWN_LOCALITY_SCHEME, locality = LOCALITY_CODE))
    }

    @Test
    fun `Locality by scheme does not exist (unknown region code)`() {
        initData()
        assertFalse(repository.existsBy(scheme = LOCALITY_SCHEME, locality = UNKNOWN_LOCALITY_CODE))
    }

    @Test
    fun `Locality by scheme does not exist (database is empty)`() {
        assertFalse(repository.existsBy(scheme = LOCALITY_SCHEME, locality = LOCALITY_CODE))
    }

    @Test
    fun `Locality by scheme and region exists`() {
        initData()
        assertTrue(repository.existsBy(scheme = LOCALITY_SCHEME, locality = LOCALITY_CODE, region = REGION_CODE))
    }

    @Test
    fun `Locality by scheme and region does not exist (unknown scheme)`() {
        initData()
        assertFalse(
            repository.existsBy(
                scheme = UNKNOWN_LOCALITY_SCHEME,
                locality = LOCALITY_CODE,
                region = REGION_CODE
            )
        )
    }

    @Test
    fun `Locality by scheme and region does not exist (unknown region code)`() {
        initData()
        assertFalse(
            repository.existsBy(
                scheme = LOCALITY_SCHEME,
                locality = LOCALITY_CODE,
                region = UNKNOWN_REGION_CODE
            )
        )
    }

    @Test
    fun `Locality by scheme and region does not exist (unknown locality code)`() {
        initData()
        assertFalse(
            repository.existsBy(
                scheme = LOCALITY_SCHEME,
                locality = UNKNOWN_LOCALITY_CODE,
                region = REGION_CODE
            )
        )
    }

    @Test
    fun `Locality by scheme and region does not exist (database is empty)`() {
        assertFalse(repository.existsBy(scheme = LOCALITY_SCHEME, locality = LOCALITY_CODE, region = REGION_CODE))
    }

    @Test
    fun `Finding locality is successful`() {
        initData()

        val actual = repository.findBy(
            locality = LOCALITY_CODE,
            scheme = LOCALITY_SCHEME,
            language = LANGUAGE_CODE,
            region = REGION_CODE
        )

        val expected = LocalityEntity(
            scheme = LOCALITY_SCHEME.value.toUpperCase(),
            id = LOCALITY_CODE.value.toUpperCase(),
            description = DESCRIPTION,
            uri = URI
        )

        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun `Finding locality fails (unknown locality code)`() {
        initData()

        val actual = repository.findBy(
            locality = UNKNOWN_LOCALITY_CODE,
            scheme = LOCALITY_SCHEME,
            language = LANGUAGE_CODE,
            region = REGION_CODE
        )

        assertTrue(actual == null)
    }

    @Test
    fun `Finding locality fails (unknown scheme)`() {
        initData()

        val actual = repository.findBy(
            locality = LOCALITY_CODE,
            scheme = UNKNOWN_LOCALITY_SCHEME,
            language = LANGUAGE_CODE,
            region = REGION_CODE
        )

        assertTrue(actual == null)
    }

    @Test
    fun `Finding locality fails (unknown language)`() {
        initData()

        val actual = repository.findBy(
            locality = LOCALITY_CODE,
            scheme = LOCALITY_SCHEME,
            language = UNKNOWN_LANGUAGE_CODE,
            region = REGION_CODE
        )

        assertTrue(actual == null)
    }

    @Test
    fun `Finding locality fails (unknown region)`() {
        initData()

        val actual = repository.findBy(
            locality = LOCALITY_CODE,
            scheme = LOCALITY_SCHEME,
            language = LANGUAGE_CODE,
            region = UNKNOWN_REGION_CODE
        )

        assertTrue(actual == null)
    }

    @Test
    fun `Finding region fails (no description is found)`() {
        initData()

        val actual = repository.findBy(
            locality = LOCALITY_WITH_NO_DESCRIPTION,
            scheme = LOCALITY_SCHEME,
            language = LANGUAGE_CODE,
            region = UNKNOWN_REGION_CODE
        )

        assertTrue(actual == null)
    }
}
