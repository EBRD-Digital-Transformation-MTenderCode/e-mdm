package com.procurement.mdm.application.service.requirement.group

import com.procurement.mdm.domain.model.Phase
import com.procurement.mdm.domain.model.Pmd
import com.procurement.mdm.domain.model.code.CountryCode
import com.procurement.mdm.domain.model.code.CriterionCode
import com.procurement.mdm.domain.model.code.LanguageCode
import com.procurement.mdm.domain.model.identifier.RequirementGroupIdentifier
import com.procurement.mdm.domain.repository.requirement.group.RequirementGroupRepository
import org.springframework.stereotype.Service

interface RequirementGroupService {
    fun getAll(
        country: String, pmd: String, language: String, phase: String, criterion: String
    ): List<RequirementGroupIdentifier>
}

@Service
class RequirementGroupServiceImpl(
    private val requirementGroupRepository: RequirementGroupRepository
) : RequirementGroupService {

    override fun getAll(
        country: String, pmd: String, language: String, phase: String, criterion: String
    ): List<RequirementGroupIdentifier> {
        val countryCode = CountryCode(country)
        val languageCode = LanguageCode(language)
        val criteriaPmd = Pmd(pmd)
        val criteriaPhase = Phase(phase)
        val criterionCode = CriterionCode(criterion)

        return requirementGroupRepository.findBy(
            country = countryCode,
            language = languageCode,
            pmd = criteriaPmd,
            phase = criteriaPhase,
            criterion = criterionCode
        ).map { entity ->
            RequirementGroupIdentifier(
                id = entity.id,
                description = entity.description
            )
        }
    }
}
