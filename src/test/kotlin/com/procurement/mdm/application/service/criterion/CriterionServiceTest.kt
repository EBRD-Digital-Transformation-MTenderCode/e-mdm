package com.procurement.mdm.application.service.criterion

import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import com.procurement.mdm.domain.entity.CriterionEntity
import com.procurement.mdm.domain.model.Phase
import com.procurement.mdm.domain.model.Pmd
import com.procurement.mdm.domain.model.code.CountryCode
import com.procurement.mdm.domain.model.code.LanguageCode
import com.procurement.mdm.domain.model.identifier.CriterionIdentifier
import com.procurement.mdm.domain.repository.criterion.CriterionRepository
import com.procurement.mdm.domain.repository.criterion.StandardCriterionRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CriterionServiceTest {

    companion object {
        private const val LANGUAGE = "en"
        private val LANGUAGE_CODE = LanguageCode(LANGUAGE)

        private const val COUNTRY = "md"
        private val COUNTRY_CODE = CountryCode(COUNTRY)

        private const val PMD = "ot"
        private val CRITERION_PMD = Pmd(PMD)

        private const val PHASE = "submission"
        private val CRITERION_PHASE = Phase(PHASE)

        private val FIRST_CRITERION_ENTITY = CriterionEntity(
            id = "MD_OT_1",
            description = "criterion-description-1",
            title = "criterion-title-1",
            classification = CriterionEntity.Classification(
                id = "classification-id-1",
                scheme = "classification-scheme-1"
            )
        )

        private val SECOND_CRITERION_ENTITY = CriterionEntity(
            id = "MD_OT_2",
            description = "criterion-description-2",
            title = "criterion-title-2",
            classification = CriterionEntity.Classification(
                id = "classification-id-1",
                scheme = "classification-scheme-1"
            )
        )
    }

    private lateinit var criterionRepository: CriterionRepository
    private val standardCriterionRepository: StandardCriterionRepository = mock()
    private lateinit var service: CriterionService

    @BeforeEach
    fun init() {
        criterionRepository = mock()
        service = CriterionServiceImpl(criterionRepository, standardCriterionRepository)
    }

    @Test
    fun `Getting all criteria is successful`() {
        val storedCriterion = listOf(FIRST_CRITERION_ENTITY, SECOND_CRITERION_ENTITY)
        whenever(
            criterionRepository.findBy(
                country = eq(COUNTRY_CODE),
                pmd = eq(CRITERION_PMD),
                language = eq(LANGUAGE_CODE),
                phase = eq(CRITERION_PHASE)
            )
        )
            .thenReturn(storedCriterion)

        val actual = service.getAll(country = COUNTRY, phase = PHASE, pmd = PMD, language = LANGUAGE)

        val expected = storedCriterion.map { entity ->
            CriterionIdentifier(
                id = entity.id, title = entity.title, description = entity.description,
                classification = entity.classification.let { classification ->
                    CriterionIdentifier.Classification(
                        id = classification.id,
                        scheme = classification.scheme
                    )
                }
            )
        }

        assertEquals(expected, actual)
    }

    @Test
    fun `Getting all criteria fails (criteria not found)`() {
        whenever(
            criterionRepository.findBy(
                country = eq(COUNTRY_CODE),
                pmd = eq(CRITERION_PMD),
                language = eq(LANGUAGE_CODE),
                phase = eq(CRITERION_PHASE)
            )
        )
            .thenReturn(emptyList())

        val actual = service.getAll(country = COUNTRY, phase = PHASE, pmd = PMD, language = LANGUAGE)

        assertTrue(actual.isEmpty())
    }
}
