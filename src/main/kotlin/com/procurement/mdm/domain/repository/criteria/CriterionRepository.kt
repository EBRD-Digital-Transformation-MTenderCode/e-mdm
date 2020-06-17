package com.procurement.mdm.domain.repository.criteria

import com.procurement.mdm.domain.entity.CriterionEntity
import com.procurement.mdm.domain.model.Phase
import com.procurement.mdm.domain.model.Pmd
import com.procurement.mdm.domain.model.code.CountryCode
import com.procurement.mdm.domain.model.code.LanguageCode

interface CriterionRepository {
    fun findBy(country: CountryCode, pmd: Pmd, language: LanguageCode, phase: Phase): List<CriterionEntity>
}
