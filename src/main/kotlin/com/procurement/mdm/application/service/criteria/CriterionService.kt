package com.procurement.mdm.application.service.criteria

import com.procurement.mdm.domain.model.Phase
import com.procurement.mdm.domain.model.Pmd
import com.procurement.mdm.domain.model.code.CountryCode
import com.procurement.mdm.domain.model.code.LanguageCode
import com.procurement.mdm.domain.model.identifier.CriteriaIdentifier
import com.procurement.mdm.domain.repository.criteria.CriterionRepository
import org.springframework.stereotype.Service

interface CriterionService {
    fun getAll(country: String, pmd: String, language: String, phase: String): List<CriteriaIdentifier>
}

@Service
class CriterionServiceImpl(
    private val criterionRepository: CriterionRepository
) : CriterionService {

    override fun getAll(country: String, pmd: String, language: String, phase: String): List<CriteriaIdentifier> {
        val countryCode = CountryCode(country)
        val languageCode = LanguageCode(language)
        val criteriaPmd = Pmd(pmd)
        val criteriaPhase = Phase(phase)

        return criterionRepository.findBy(
            country = countryCode, language = languageCode, pmd = criteriaPmd, phase = criteriaPhase
        ).map { criteria ->
            CriteriaIdentifier(
                id = criteria.id,
                title = criteria.title,
                description = criteria.description
            )
        }
    }
}
