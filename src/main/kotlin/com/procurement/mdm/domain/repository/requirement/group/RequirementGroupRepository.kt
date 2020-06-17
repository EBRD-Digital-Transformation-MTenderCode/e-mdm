package com.procurement.mdm.domain.repository.requirement.group

import com.procurement.mdm.domain.entity.RequirementGroupEntity
import com.procurement.mdm.domain.model.Phase
import com.procurement.mdm.domain.model.Pmd
import com.procurement.mdm.domain.model.code.CountryCode
import com.procurement.mdm.domain.model.code.CriterionCode
import com.procurement.mdm.domain.model.code.LanguageCode

interface RequirementGroupRepository {
    fun findBy(
        country: CountryCode, pmd: Pmd, language: LanguageCode, phase: Phase, criterion: CriterionCode
    ): List<RequirementGroupEntity>
}
