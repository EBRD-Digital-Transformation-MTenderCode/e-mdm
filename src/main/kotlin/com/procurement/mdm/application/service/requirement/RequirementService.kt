package com.procurement.mdm.application.service.requirement

import com.procurement.mdm.domain.model.Phase
import com.procurement.mdm.domain.model.Pmd
import com.procurement.mdm.domain.model.code.CountryCode
import com.procurement.mdm.domain.model.code.LanguageCode
import com.procurement.mdm.domain.model.code.RequirementGroupCode
import com.procurement.mdm.domain.model.identifier.RequirementIdentifier
import com.procurement.mdm.domain.repository.requirement.RequirementRepository
import org.springframework.stereotype.Service

interface RequirementService {
    fun getAll(
        country: String, pmd: String, language: String, phase: String, requirementGroup: String
    ): List<RequirementIdentifier>
}

@Service
class RequirementServiceImpl(
    private val requirementRepository: RequirementRepository
) : RequirementService {

    override fun getAll(
        country: String, pmd: String, language: String, phase: String, requirementGroup: String
    ): List<RequirementIdentifier> {
        val countryCode = CountryCode(country)
        val languageCode = LanguageCode(language)
        val requirementPmd = Pmd(pmd)
        val requirementPhase = Phase(phase)
        val requirementGroupCode = RequirementGroupCode(requirementGroup)

        return requirementRepository.findBy(
            country = countryCode,
            language = languageCode,
            pmd = requirementPmd,
            phase = requirementPhase,
            requirementGroup = requirementGroupCode
        ).map { criterion ->
            RequirementIdentifier(
                id = criterion.id,
                title = criterion.title,
                description = criterion.description
            )
        }
    }
}
