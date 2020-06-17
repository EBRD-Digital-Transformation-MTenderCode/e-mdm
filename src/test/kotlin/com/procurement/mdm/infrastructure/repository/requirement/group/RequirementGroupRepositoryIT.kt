package com.procurement.mdm.infrastructure.repository.requirement.group

import com.procurement.mdm.domain.entity.RequirementGroupEntity
import com.procurement.mdm.domain.model.Phase
import com.procurement.mdm.domain.model.Pmd
import com.procurement.mdm.domain.model.code.CountryCode
import com.procurement.mdm.domain.model.code.CriterionCode
import com.procurement.mdm.domain.model.code.LanguageCode
import com.procurement.mdm.domain.repository.requirement.group.RequirementGroupRepository
import com.procurement.mdm.infrastructure.repository.AbstractRepositoryTest
import com.procurement.mdm.infrastructure.repository.loadSql
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class RequirementGroupRepositoryIT : AbstractRepositoryTest() {
    companion object {
        private val LANGUAGE_CODE = LanguageCode("en")
        private val UNKNOWN_LANGUAGE_CODE = LanguageCode("ul")

        private val COUNTRY_CODE = CountryCode("md")
        private val UNKNOWN_COUNTRY_CODE = CountryCode("ua")

        private val REQ_GROUP_PMD = Pmd("ot")
        private val UNKNOWN_PMD = Pmd("gpa")

        private val REQ_GROUP_PHASE = Phase("submission")
        private val UNKNOWN_PHASE = Phase("awarding")

        private val REQ_GROUP_CRITERION_1 = CriterionCode("MD_OT_1")
        private val REQ_GROUP_CRITERION_2 = CriterionCode("MD_OT_2")
        private val REQ_GROUP_CRITERION_3 = CriterionCode("MD_OT_3")
        private val REQ_GROUP_UNKNOWN_CRITERION = CriterionCode("unknown-criteria")
    }

    @Autowired
    private lateinit var repository: RequirementGroupRepository

    private fun initData() {
        val sqlCountrySchemes = loadSql("sql/requirement.group/requirement_groups_init_data.sql")
        executeSQLScript(sqlCountrySchemes)
    }

    @Test
    fun `Finding requirement group is successful`() {
        initData()

        val actual = repository.findBy(
            country = COUNTRY_CODE,
            pmd = REQ_GROUP_PMD,
            language = LANGUAGE_CODE,
            phase = REQ_GROUP_PHASE,
            criterion = REQ_GROUP_CRITERION_1
        )

        val firstRequirementGroupEntity = RequirementGroupEntity(id = "REQ_1", description = "req-description-1")
        val secondRequirementGroupEntity = RequirementGroupEntity(id = "REQ_1_1", description = "req-description-1-1")
        val expected = listOf(firstRequirementGroupEntity, secondRequirementGroupEntity)

        assertEquals(expected, actual)
    }

    @Test
    fun `Finding requirement group is successful (description is null)`() {
        initData()

        val actual = repository.findBy(
            country = COUNTRY_CODE,
            pmd = REQ_GROUP_PMD,
            language = LANGUAGE_CODE,
            phase = REQ_GROUP_PHASE,
            criterion = REQ_GROUP_CRITERION_2
        )

        val requirementGroupEntity = RequirementGroupEntity(id = "REQ_2", description = null)
        val expected = listOf(requirementGroupEntity)

        assertEquals(expected, actual)
    }

    @Test
    fun `Finding requirement group is successful (no description for one record)`() {
        initData()

        val actual = repository.findBy(
            country = COUNTRY_CODE,
            pmd = REQ_GROUP_PMD,
            language = LANGUAGE_CODE,
            phase = REQ_GROUP_PHASE,
            criterion = REQ_GROUP_CRITERION_3
        )

        val requirementGroupEntity = RequirementGroupEntity(id = "REQ_3", description = null)

        val expected = listOf(requirementGroupEntity)

        assertEquals(expected, actual)
    }

    @Test
    fun `Finding criterion is successful (unknown country)`() {
        initData()

        val actual = repository.findBy(
            country = UNKNOWN_COUNTRY_CODE,
            pmd = REQ_GROUP_PMD,
            language = LANGUAGE_CODE,
            phase = REQ_GROUP_PHASE,
            criterion = REQ_GROUP_CRITERION_1
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
            phase = REQ_GROUP_PHASE,
            criterion = REQ_GROUP_CRITERION_1
        )

        assertTrue(actual.isEmpty())
    }

    @Test
    fun `Finding criterion is successful (unknown language)`() {
        initData()

        val actual = repository.findBy(
            country = COUNTRY_CODE,
            pmd = REQ_GROUP_PMD,
            language = UNKNOWN_LANGUAGE_CODE,
            phase = REQ_GROUP_PHASE,
            criterion = REQ_GROUP_CRITERION_1
        )

        assertTrue(actual.isEmpty())
    }

    @Test
    fun `Finding criterion is successful (unknown phase)`() {
        initData()

        val actual = repository.findBy(
            country = COUNTRY_CODE,
            pmd = REQ_GROUP_PMD,
            language = LANGUAGE_CODE,
            phase = UNKNOWN_PHASE,
            criterion = REQ_GROUP_CRITERION_1
        )

        assertTrue(actual.isEmpty())
    }

    @Test
    fun `Finding criterion is successful (unknown criterion)`() {
        initData()

        val actual = repository.findBy(
            country = COUNTRY_CODE,
            pmd = REQ_GROUP_PMD,
            language = LANGUAGE_CODE,
            phase = UNKNOWN_PHASE,
            criterion = REQ_GROUP_UNKNOWN_CRITERION
        )

        assertTrue(actual.isEmpty())
    }
}
