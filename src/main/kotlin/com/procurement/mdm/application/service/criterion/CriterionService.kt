package com.procurement.mdm.application.service.criterion

import com.procurement.mdm.domain.model.Phase
import com.procurement.mdm.domain.model.Pmd
import com.procurement.mdm.domain.model.code.CountryCode
import com.procurement.mdm.domain.model.code.LanguageCode
import com.procurement.mdm.domain.model.identifier.CriterionIdentifier
import com.procurement.mdm.domain.repository.criteria.CriterionRepository
import org.springframework.stereotype.Service

interface CriterionService {
    fun getAll(country: String, pmd: String, language: String, phase: String): List<CriterionIdentifier>
}

@Service
class CriterionServiceImpl(
    private val criterionRepository: CriterionRepository
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
}
