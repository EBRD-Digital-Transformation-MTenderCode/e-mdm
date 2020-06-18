package com.procurement.mdm.infrastructure.repository.requirement

import com.procurement.mdm.domain.entity.RequirementEntity
import com.procurement.mdm.domain.model.Phase
import com.procurement.mdm.domain.model.Pmd
import com.procurement.mdm.domain.model.code.CountryCode
import com.procurement.mdm.domain.model.code.LanguageCode
import com.procurement.mdm.domain.model.code.RequirementGroupCode
import com.procurement.mdm.domain.repository.requirement.RequirementRepository
import com.procurement.mdm.infrastructure.repository.AbstractRepositoryTest
import com.procurement.mdm.infrastructure.repository.loadSql
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class RequirementRepositoryIT : AbstractRepositoryTest() {
    companion object {
        private val LANGUAGE_CODE = LanguageCode("en")
        private val UNKNOWN_LANGUAGE_CODE = LanguageCode("ul")

        private val COUNTRY_CODE = CountryCode("md")
        private val UNKNOWN_COUNTRY_CODE = CountryCode("ua")

        private val REQ_PMD = Pmd("ot")
        private val UNKNOWN_PMD = Pmd("gpa")

        private val REQ_PHASE = Phase("submission")
        private val UNKNOWN_PHASE = Phase("awarding")

        private val REQ_GROUP_CODE_1 = RequirementGroupCode("REQ_GROUP_1")
        private val REQ_GROUP_CODE_2 = RequirementGroupCode("REQ_GROUP_2")
        private val REQ_GROUP_CODE_3 = RequirementGroupCode("REQ_GROUP_3")
        private val UNKNOWN_REQ_GROUP_CODE = RequirementGroupCode("unknown-req-group")
    }

    @Autowired
    private lateinit var repository: RequirementRepository

    private fun initData() {
        val sqlRequirements = loadSql("sql/requirement/requirements_init_data.sql")
        executeSQLScript(sqlRequirements)
    }

    @Test
    fun `Finding requirements is successful`() {
        initData()

        val actual = repository.findBy(
            country = COUNTRY_CODE,
            pmd = REQ_PMD,
            language = LANGUAGE_CODE,
            phase = REQ_PHASE,
            requirementGroup = REQ_GROUP_CODE_1
        )

        val firstRequirementEntity = RequirementEntity(id = "REQ_1", title = "req-title-1", description = "req-description-1")
        val secondRequirementEntity = RequirementEntity(id = "REQ_1_1", title = "req-title-1-1", description = "req-description-1-1")
        val expected = listOf(firstRequirementEntity, secondRequirementEntity)

        assertEquals(expected, actual)
    }

    @Test
    fun `Finding requirements is successful (description is null)`() {
        initData()

        val actual = repository.findBy(
            country = COUNTRY_CODE,
            pmd = REQ_PMD,
            language = LANGUAGE_CODE,
            phase = REQ_PHASE,
            requirementGroup = REQ_GROUP_CODE_2
        )

        val requirementEntity = RequirementEntity(id = "REQ_2", title = "req-title-2", description = null)
        val expected = listOf(requirementEntity)

        assertEquals(expected, actual)
    }

    @Test
    fun `Finding requirements is successful (no description for one record)`() {
        initData()

        val actual = repository.findBy(
            country = COUNTRY_CODE,
            pmd = REQ_PMD,
            language = LANGUAGE_CODE,
            phase = REQ_PHASE,
            requirementGroup = REQ_GROUP_CODE_3
        )

        assertTrue(actual.isEmpty())
    }

    @Test
    fun `Finding requirements is successful (unknown country)`() {
        initData()

        val actual = repository.findBy(
            country = UNKNOWN_COUNTRY_CODE,
            pmd = REQ_PMD,
            language = LANGUAGE_CODE,
            phase = REQ_PHASE,
            requirementGroup = REQ_GROUP_CODE_1
        )

        assertTrue(actual.isEmpty())
    }

    @Test
    fun `Finding requirements is successful (unknown pmd)`() {
        initData()

        val actual = repository.findBy(
            country = COUNTRY_CODE,
            pmd = UNKNOWN_PMD,
            language = LANGUAGE_CODE,
            phase = REQ_PHASE,
            requirementGroup = REQ_GROUP_CODE_1
        )

        assertTrue(actual.isEmpty())
    }

    @Test
    fun `Finding requirements is successful (unknown language)`() {
        initData()

        val actual = repository.findBy(
            country = COUNTRY_CODE,
            pmd = REQ_PMD,
            language = UNKNOWN_LANGUAGE_CODE,
            phase = REQ_PHASE,
            requirementGroup = REQ_GROUP_CODE_1
        )

        assertTrue(actual.isEmpty())
    }

    @Test
    fun `Finding requirements is successful (unknown phase)`() {
        initData()

        val actual = repository.findBy(
            country = COUNTRY_CODE,
            pmd = REQ_PMD,
            language = LANGUAGE_CODE,
            phase = UNKNOWN_PHASE,
            requirementGroup = REQ_GROUP_CODE_1
        )

        assertTrue(actual.isEmpty())
    }

    @Test
    fun `Finding requirements is successful (unknown requirement group)`() {
        initData()

        val actual = repository.findBy(
            country = COUNTRY_CODE,
            pmd = REQ_PMD,
            language = LANGUAGE_CODE,
            phase = UNKNOWN_PHASE,
            requirementGroup = UNKNOWN_REQ_GROUP_CODE
        )

        assertTrue(actual.isEmpty())
    }
}
