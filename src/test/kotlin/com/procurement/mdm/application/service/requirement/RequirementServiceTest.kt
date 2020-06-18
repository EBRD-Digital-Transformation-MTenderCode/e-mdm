package com.procurement.mdm.application.service.requirement

import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import com.procurement.mdm.domain.entity.RequirementEntity
import com.procurement.mdm.domain.model.Phase
import com.procurement.mdm.domain.model.Pmd
import com.procurement.mdm.domain.model.code.CountryCode
import com.procurement.mdm.domain.model.code.LanguageCode
import com.procurement.mdm.domain.model.code.RequirementGroupCode
import com.procurement.mdm.domain.model.identifier.RequirementIdentifier
import com.procurement.mdm.domain.repository.requirement.RequirementRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class RequirementServiceTest {

    companion object {
        private const val LANGUAGE = "en"
        private val LANGUAGE_CODE = LanguageCode(LANGUAGE)

        private const val COUNTRY = "md"
        private val COUNTRY_CODE = CountryCode(COUNTRY)

        private const val PMD = "ot"
        private val REQ_PMD = Pmd(PMD)

        private const val PHASE = "submission"
        private val REQ_PHASE = Phase(PHASE)

        private const val REQ_GROUP = "REQ_GROUP_1"
        private val  REQ_GROUP_CODE = RequirementGroupCode(REQ_GROUP)

        private val FIRST_REQ_ENTITY = RequirementEntity(id = "REQ_1", title = "req-title-1", description = "req-description-1")
        private val SECOND_REQ_ENTITY = RequirementEntity(id = "REQ_2", title = "req-title-2", description = "req-description-2")
    }

    private lateinit var requirementRepository: RequirementRepository
    private lateinit var service: RequirementService

    @BeforeEach
    fun init() {
        requirementRepository = mock()
        service = RequirementServiceImpl(requirementRepository)
    }

    @Test
    fun `Getting all requirements is successful`() {
        val storedRequirements = listOf(FIRST_REQ_ENTITY, SECOND_REQ_ENTITY)
        whenever(
            requirementRepository.findBy(
                country = eq(COUNTRY_CODE),
                pmd = eq(REQ_PMD),
                language = eq(LANGUAGE_CODE),
                phase = eq(REQ_PHASE),
                requirementGroup = eq(REQ_GROUP_CODE)
            )
        )
            .thenReturn(storedRequirements)

        val actual = service.getAll(
            country = COUNTRY, phase = PHASE, pmd = PMD, language = LANGUAGE, requirementGroup = REQ_GROUP
        )

        val expected = storedRequirements.map { entity ->
            RequirementIdentifier(id = entity.id, title = entity.title, description = entity.description)
        }

        assertEquals(expected, actual)
    }

    @Test
    fun `Getting all requirements fails (requirements not found)`() {
        whenever(
            requirementRepository.findBy(
                country = eq(COUNTRY_CODE),
                pmd = eq(REQ_PMD),
                language = eq(LANGUAGE_CODE),
                phase = eq(REQ_PHASE),
                requirementGroup = eq(REQ_GROUP_CODE)
            )
        )
            .thenReturn(emptyList())

        val actual = service.getAll(
            country = COUNTRY, phase = PHASE, pmd = PMD, language = LANGUAGE, requirementGroup = REQ_GROUP
        )

        assertTrue(actual.isEmpty())
    }
}
