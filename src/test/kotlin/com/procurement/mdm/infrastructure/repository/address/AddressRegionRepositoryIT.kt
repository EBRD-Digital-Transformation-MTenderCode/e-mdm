package com.procurement.mdm.infrastructure.repository.address

import com.procurement.mdm.domain.entity.RegionEntity
import com.procurement.mdm.domain.model.code.CountryCode
import com.procurement.mdm.domain.model.code.LanguageCode
import com.procurement.mdm.domain.model.code.RegionCode
import com.procurement.mdm.domain.repository.address.AddressRegionRepository
import com.procurement.mdm.infrastructure.repository.AbstractRepositoryTest
import com.procurement.mdm.infrastructure.repository.loadSql
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class AddressRegionRepositoryIT : AbstractRepositoryTest() {
    companion object {
        private val LANGUAGE_CODE = LanguageCode("ro")
        private val UNKNOWN_LANGUAGE_CODE = LanguageCode("ul")

        private val COUNTRY_CODE = CountryCode("md")
        private val UNKNOWN_COUNTRY_CODE = CountryCode("uc")

        private val REGION_CODE = RegionCode("region-1")
        private val UNKNOWN_REGION_CODE = RegionCode("UNKNOWN")

        private const val SCHEME_CODE = "CUATM"
        private const val DESCRIPTION = "Anenii Noi RO"
        private const val URI = "http://statistica.md"
    }

    @Autowired
    private lateinit var repository: AddressRegionRepository

    private fun initData() {
        val sqlLanguages = loadSql("sql/languages_init_data.sql")
        executeSQLScript(sqlLanguages)

        val sqlSchemes = loadSql("sql/list_schemes_init_data.sql")
        executeSQLScript(sqlSchemes)

        val sqlCountries = loadSql("sql/address/countries_init_data.sql")
        executeSQLScript(sqlCountries)

        val sqlRegions = loadSql("sql/address/regions_init_data.sql")
        executeSQLScript(sqlRegions)
    }

    @Test
    fun `Finding region by code is successful`() {
        initData()

        val actual = repository.findBy(
            region = REGION_CODE,
            country = COUNTRY_CODE,
            language = LANGUAGE_CODE
        )

        val expected = RegionEntity(
            scheme = SCHEME_CODE,
            id = REGION_CODE.value.toUpperCase(),
            description = DESCRIPTION,
            uri = URI
        )

        assertEquals(expected, actual)
    }

    @Test
    fun `Finding region by code is error (unknown id)`() {
        initData()

        val region = repository.findBy(
            region = UNKNOWN_REGION_CODE,
            country = COUNTRY_CODE,
            language = LANGUAGE_CODE
        )

        assertNull(region)
    }

    @Test
    fun `Finding region by code is error (unknown country)`() {
        initData()

        val region = repository.findBy(
            region = REGION_CODE,
            country = UNKNOWN_COUNTRY_CODE,
            language = LANGUAGE_CODE
        )

        assertNull(region)
    }

    @Test
    fun `Finding region by code is error (unknown language)`() {
        initData()

        val region = repository.findBy(
            region = REGION_CODE,
            country = COUNTRY_CODE,
            language = UNKNOWN_LANGUAGE_CODE
        )

        assertNull(region)
    }

    @Test
    fun `Finding region by code is error (database is empty)`() {
        val region = repository.findBy(
            region = REGION_CODE,
            country = COUNTRY_CODE,
            language = LANGUAGE_CODE
        )

        assertNull(region)
    }
}
