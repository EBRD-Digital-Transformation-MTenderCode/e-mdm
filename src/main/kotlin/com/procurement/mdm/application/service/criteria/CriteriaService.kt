package com.procurement.mdm.application.service.criteria

import com.procurement.mdm.domain.model.Phase
import com.procurement.mdm.domain.model.Pmd
import com.procurement.mdm.domain.model.code.CountryCode
import com.procurement.mdm.domain.model.code.LanguageCode
import com.procurement.mdm.domain.model.identifier.CriteriaIdentifier
import com.procurement.mdm.domain.repository.criteria.CriteriaRepository
import org.springframework.stereotype.Service

interface CriteriaService {
    fun getAll(country: String, pmd: String, language: String, phase: String): List<CriteriaIdentifier>
}

@Service
class CriteriaServiceImpl(
    private val criteriaRepository: CriteriaRepository
) : CriteriaService {

    override fun getAll(country: String, pmd: String, language: String, phase: String): List<CriteriaIdentifier> {
        val countryCode = CountryCode(country)
        val languageCode = LanguageCode(language)
        val criteriaPmd = Pmd(pmd)
        val criteriaPhase = Phase(phase)

        return criteriaRepository.findBy(
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
