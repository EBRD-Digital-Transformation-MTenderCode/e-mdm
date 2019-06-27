package com.procurement.mdm.infrastructure.repository.address

import com.procurement.mdm.domain.entity.LocalityEntity
import com.procurement.mdm.domain.model.code.CountryCode
import com.procurement.mdm.domain.model.code.LanguageCode
import com.procurement.mdm.domain.model.code.LocalityCode
import com.procurement.mdm.domain.model.code.RegionCode
import com.procurement.mdm.domain.repository.address.AddressLocalityRepository
import com.procurement.mdm.infrastructure.repository.AbstractRepositoryTest
import com.procurement.mdm.infrastructure.repository.loadSql
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class AddressLocalityRepositoryIT : AbstractRepositoryTest() {
    companion object {
        private val LANGUAGE_CODE = LanguageCode("ro")
        private val UNKNOWN_LANGUAGE_CODE = LanguageCode("ul")

        private val COUNTRY_CODE = CountryCode("md")
        private val UNKNOWN_COUNTRY_CODE = CountryCode("uc")

        private val REGION_CODE = RegionCode("region-1")
        private val UNKNOWN_REGION_CODE = RegionCode("UNKNOWN")

        private val LOCALITY_CODE = LocalityCode("locality-1")
        private val UNKNOWN_LOCALITY_CODE = LocalityCode("UNKNOWN")

        private const val SCHEME_CODE = "CUATM"
        private const val DESCRIPTION = "mun.Chişinău RO"
        private const val URI = "http://statistica.md"
    }

    @Autowired
    private lateinit var repository: AddressLocalityRepository

    private fun initData() {
        val sqlLanguages = loadSql("sql/languages_init_data.sql")
        executeSQLScript(sqlLanguages)

        val sqlSchemes = loadSql("sql/list_schemes_init_data.sql")
        executeSQLScript(sqlSchemes)

        val sqlCountries = loadSql("sql/address/countries_init_data.sql")
        executeSQLScript(sqlCountries)

        val sqlRegions = loadSql("sql/address/regions_init_data.sql")
        executeSQLScript(sqlRegions)

        val sqlLocalities = loadSql("sql/address/localities_init_data.sql")
        executeSQLScript(sqlLocalities)
    }

    @Test
    fun `Finding locality by code is successful`() {
        initData()

        val actual = repository.findBy(
            locality = LOCALITY_CODE,
            country = COUNTRY_CODE,
            region = REGION_CODE,
            language = LANGUAGE_CODE
        )

        val expected = LocalityEntity(
            scheme = SCHEME_CODE,
            id = LOCALITY_CODE.value.toUpperCase(),
            description = DESCRIPTION,
            uri = URI
        )

        assertEquals(expected, actual)
    }

    @Test
    fun `Finding locality by code is error (unknown id)`() {
        initData()

        val locality = repository.findBy(
            locality = UNKNOWN_LOCALITY_CODE,
            country = COUNTRY_CODE,
            region = REGION_CODE,
            language = LANGUAGE_CODE
        )

        assertNull(locality)
    }

    @Test
    fun `Finding locality by code is error (unknown country)`() {
        initData()

        val locality = repository.findBy(
            locality = LOCALITY_CODE,
            country = UNKNOWN_COUNTRY_CODE,
            region = REGION_CODE,
            language = LANGUAGE_CODE
        )

        assertNull(locality)
    }

    @Test
    fun `Finding locality by code is error (unknown region)`() {
        initData()

        val locality = repository.findBy(
            locality = LOCALITY_CODE,
            country = COUNTRY_CODE,
            region = UNKNOWN_REGION_CODE,
            language = LANGUAGE_CODE
        )

        assertNull(locality)
    }

    @Test
    fun `Finding locality by code is error (unknown language)`() {
        initData()

        val locality = repository.findBy(
            locality = LOCALITY_CODE,
            country = COUNTRY_CODE,
            region = REGION_CODE,
            language = UNKNOWN_LANGUAGE_CODE
        )

        assertNull(locality)
    }

    @Test
    fun `Finding locality by code is error (database is empty)`() {
        val locality = repository.findBy(
            locality = LOCALITY_CODE,
            country = COUNTRY_CODE,
            region = REGION_CODE,
            language = LANGUAGE_CODE
        )

        assertNull(locality)
    }
}
