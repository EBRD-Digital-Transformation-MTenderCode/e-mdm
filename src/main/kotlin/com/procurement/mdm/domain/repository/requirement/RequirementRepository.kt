package com.procurement.mdm.domain.repository.requirement

import com.procurement.mdm.domain.entity.RequirementEntity
import com.procurement.mdm.domain.model.Phase
import com.procurement.mdm.domain.model.Pmd
import com.procurement.mdm.domain.model.code.CountryCode
import com.procurement.mdm.domain.model.code.LanguageCode
import com.procurement.mdm.domain.model.code.RequirementGroupCode

interface RequirementRepository {
    fun findBy(
        country: CountryCode, pmd: Pmd, language: LanguageCode, phase: Phase, requirementGroup: RequirementGroupCode
    ): List<RequirementEntity>
}
