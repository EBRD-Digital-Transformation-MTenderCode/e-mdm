package com.procurement.mdm.application.service.criterion

import com.procurement.mdm.domain.model.Phase
import com.procurement.mdm.domain.model.Pmd
import com.procurement.mdm.domain.model.ProcurementCategory
import com.procurement.mdm.domain.model.code.CountryCode
import com.procurement.mdm.domain.model.code.LanguageCode
import com.procurement.mdm.domain.model.criterion.CriterionCategory
import com.procurement.mdm.domain.model.criterion.model.StandardCriteriaResult
import com.procurement.mdm.domain.model.identifier.CriterionIdentifier
import com.procurement.mdm.domain.repository.criterion.CriterionRepository
import com.procurement.mdm.domain.repository.criterion.StandardCriterionRepository
import com.procurement.mdm.exception.ErrorType
import com.procurement.mdm.exception.ExErrorException
import com.procurement.mdm.exception.InErrorException
import com.procurement.mdm.model.record.StandardCriterionRecord
import org.springframework.stereotype.Service

interface CriterionService {
    fun getAll(country: String, pmd: String, language: String, phase: String): List<CriterionIdentifier>
    fun getStandard(country: String, language: String, mainProcurementCategory: String?, criteriaCategory: String?): StandardCriteriaResult
}

@Service
class CriterionServiceImpl(
    private val criterionRepository: CriterionRepository,
    private val standardCriterionRepository: StandardCriterionRepository
) : CriterionService {

    override fun getAll(country: String, pmd: String, language: String, phase: String): List<CriterionIdentifier> {
        val countryCode = CountryCode(country)
        val languageCode = LanguageCode(language)
        val criterionPmd = Pmd(pmd)
        val criterionPhase = Phase(phase)

        return criterionRepository.findBy(
            country = countryCode, language = languageCode, pmd = criterionPmd, phase = criterionPhase
        ).map { criterion ->
            CriterionIdentifier(
                id = criterion.id,
                title = criterion.title,
                description = criterion.description
            )
        }
    }

    override fun getStandard(
        country: String,
        language: String,
        mainProcurementCategory: String?,
        criteriaCategory: String?
    ): StandardCriteriaResult {
        val countryCode = CountryCode(country)
        val languageCode = LanguageCode(language)
        val parsedProcurementCategory = mainProcurementCategory?.let { parseProcurementCategory(it) }
        val parsedCriteriaCategory = criteriaCategory?.let { parseCriteriaCategory(it) }

        val standardCriteriaRecords: List<StandardCriterionRecord> = when {
            parsedProcurementCategory == null && parsedCriteriaCategory == null ->
                standardCriterionRepository.findBy(countryCode, languageCode)

            parsedProcurementCategory != null && parsedCriteriaCategory == null ->
                standardCriterionRepository.findBy(countryCode, languageCode, parsedProcurementCategory)

            parsedProcurementCategory == null && parsedCriteriaCategory != null ->
                standardCriterionRepository.findBy(countryCode, languageCode, parsedCriteriaCategory)

            parsedProcurementCategory != null && parsedCriteriaCategory != null ->
                standardCriterionRepository.findBy(countryCode, languageCode, parsedProcurementCategory, parsedCriteriaCategory)

            else -> emptyList()
        }


        return standardCriteriaRecords
            .map { StandardCriterionRecord.toEntity(it) }
            .map { StandardCriteriaResult.fromEntity(it) }
            .let { StandardCriteriaResult(it) }

    }

    private fun parseCriteriaCategory(category: String): CriterionCategory =
        when (category.toLowerCase()) {
            "selection" -> CriterionCategory.SELECTION
            "exclusion" -> CriterionCategory.EXCLUSION
            "other" -> CriterionCategory.OTHER
            else -> throw ExErrorException(ErrorType.INVALID_CRITERIA_CATEGORY)
        }

    private fun parseProcurementCategory(category: String): ProcurementCategory =
        try {
            ProcurementCategory.fromValue(category)
        } catch (ex: InErrorException) {
            throw ExErrorException(ErrorType.INVALID_PROCUREMENT_CATEGORY)
        }

}
