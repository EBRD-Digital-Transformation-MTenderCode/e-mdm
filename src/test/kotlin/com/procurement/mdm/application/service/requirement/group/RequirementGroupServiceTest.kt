package com.procurement.mdm.application.service.requirement.group

import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import com.procurement.mdm.domain.entity.RequirementGroupEntity
import com.procurement.mdm.domain.model.Phase
import com.procurement.mdm.domain.model.Pmd
import com.procurement.mdm.domain.model.code.CountryCode
import com.procurement.mdm.domain.model.code.CriterionCode
import com.procurement.mdm.domain.model.code.LanguageCode
import com.procurement.mdm.domain.model.identifier.RequirementGroupIdentifier
import com.procurement.mdm.domain.repository.requirement.group.RequirementGroupRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class RequirementGroupServiceTest {

    companion object {
        private const val LANGUAGE = "en"
        private val LANGUAGE_CODE = LanguageCode(LANGUAGE)

        private const val COUNTRY = "md"
        private val COUNTRY_CODE = CountryCode(COUNTRY)

        private const val PMD = "ot"
        private val REQ_GROUP_PMD = Pmd(PMD)

        private const val PHASE = "submission"
        private val REQ_GROUP_PHASE = Phase(PHASE)

        private const val CRITERION = "MD_OT_1"
        private val REQ_GROUP_CRITERION = CriterionCode(CRITERION)

        private val FIRST_REQ_GROUP_ENTITY = RequirementGroupEntity(id = "REQ_1", description = "req-description-1")
        private val SECOND_REQ_GROUP_ENTITY = RequirementGroupEntity(id = "REQ_2", description = "req-description-2")
    }

    private lateinit var requirementGroupRepository: RequirementGroupRepository
    private lateinit var service: RequirementGroupService

    @BeforeEach
    fun init() {
        requirementGroupRepository = mock()
        service = RequirementGroupServiceImpl(requirementGroupRepository)
    }

    @Test
    fun `Getting all requirement groups is successful`() {
        val storedReqGroups = listOf(FIRST_REQ_GROUP_ENTITY, SECOND_REQ_GROUP_ENTITY)
        whenever(
            requirementGroupRepository.findBy(
                country = eq(COUNTRY_CODE),
                pmd = eq(REQ_GROUP_PMD),
                language = eq(LANGUAGE_CODE),
                phase = eq(REQ_GROUP_PHASE),
                criterion = eq(REQ_GROUP_CRITERION)
            )
        )
            .thenReturn(storedReqGroups)

        val actual = service.getAll(
            country = COUNTRY, phase = PHASE, pmd = PMD, language = LANGUAGE, criterion = CRITERION
        )

        val expected = storedReqGroups.map { entity ->
            RequirementGroupIdentifier(id = entity.id, description = entity.description)
        }

        assertEquals(expected, actual)
    }

    @Test
    fun `Getting all requirement groups fails (requirement groups not found)`() {
        whenever(
            requirementGroupRepository.findBy(
                country = eq(COUNTRY_CODE),
                pmd = eq(REQ_GROUP_PMD),
                language = eq(LANGUAGE_CODE),
                phase = eq(REQ_GROUP_PHASE),
                criterion = eq(REQ_GROUP_CRITERION)
            )
        )
            .thenReturn(emptyList())

        val actual = service.getAll(
            country = COUNTRY, phase = PHASE, pmd = PMD, language = LANGUAGE, criterion = CRITERION
        )

        assertTrue(actual.isEmpty())
    }
}
