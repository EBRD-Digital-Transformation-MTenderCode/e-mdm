package com.procurement.mdm.infrastructure.repository.criteria

import com.procurement.mdm.domain.entity.CriteriaEntity
import com.procurement.mdm.domain.model.Phase
import com.procurement.mdm.domain.model.Pmd
import com.procurement.mdm.domain.model.code.CountryCode
import com.procurement.mdm.domain.model.code.LanguageCode
import com.procurement.mdm.domain.repository.criteria.CriterionRepository
import com.procurement.mdm.infrastructure.repository.AbstractRepositoryTest
import com.procurement.mdm.infrastructure.repository.loadSql
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class CriterionRepositoryIT : AbstractRepositoryTest() {
    companion object {
        private val LANGUAGE_CODE = LanguageCode("en")
        private val UNKNOWN_LANGUAGE_CODE = LanguageCode("ul")

        private val COUNTRY_CODE = CountryCode("md")
        private val UNKNOWN_COUNTRY_CODE = CountryCode("ua")

        private const val PMD = "ot"
        private val CRITERIA_PMD = Pmd(PMD)
        private val UNKNOWN_PMD = Pmd("gpa")

        private const val PHASE = "submission"
        private val CRITERIA_PHASE = Phase(PHASE)
        private val UNKNOWN_PHASE = Phase("awarding")

        private val FIRST_CRITERION_ENTITY = CriteriaEntity(
            id = "MD_OT_1",
            description = "criterion-description-1",
            title = "criterion-title-1"
        )

        private val SECOND_CRITERION_ENTITY = CriteriaEntity(
            id = "MD_OT_2",
            description = "criterion-description-2",
            title = "criterion-title-2"
        )
    }

    @Autowired
    private lateinit var repository: CriterionRepository

    private fun initData() {
        val sqlCountrySchemes = loadSql("sql/criteria/criterion_init_data.sql")
        executeSQLScript(sqlCountrySchemes)
    }

    @Test
    fun `Finding criterion is successful`() {
        initData()

        val actual = repository.findBy(
            country = COUNTRY_CODE,
            pmd = CRITERIA_PMD,
            language = LANGUAGE_CODE,
            phase = CRITERIA_PHASE
        )

        val expected = listOf(FIRST_CRITERION_ENTITY, SECOND_CRITERION_ENTITY)

        assertEquals(expected, actual)
    }

    @Test
    fun `Finding criterion is successful (unknown country)`() {
        initData()

        val actual = repository.findBy(
            country = UNKNOWN_COUNTRY_CODE,
            pmd = CRITERIA_PMD,
            language = LANGUAGE_CODE,
            phase = CRITERIA_PHASE
        )

        assertTrue(actual.isEmpty())
    }

    @Test
    fun `Finding criterion is successful (unknown pmd)`() {
        initData()

        val actual = repository.findBy(
            country = COUNTRY_CODE,
            pmd = UNKNOWN_PMD,
            language = LANGUAGE_CODE,
            phase = CRITERIA_PHASE
        )

        assertTrue(actual.isEmpty())
    }

    @Test
    fun `Finding criterion is successful (unknown language)`() {
        initData()

        val actual = repository.findBy(
            country = COUNTRY_CODE,
            pmd = CRITERIA_PMD,
            language = UNKNOWN_LANGUAGE_CODE,
            phase = CRITERIA_PHASE
        )

        assertTrue(actual.isEmpty())
    }

    @Test
    fun `Finding criterion is successful (unknown phase)`() {
        initData()

        val actual = repository.findBy(
            country = COUNTRY_CODE,
            pmd = CRITERIA_PMD,
            language = LANGUAGE_CODE,
            phase = UNKNOWN_PHASE
        )

        assertTrue(actual.isEmpty())
    }
}
