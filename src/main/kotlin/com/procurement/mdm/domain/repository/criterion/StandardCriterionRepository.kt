package com.procurement.mdm.domain.repository.criterion

import com.procurement.mdm.domain.model.ProcurementCategory
import com.procurement.mdm.domain.model.code.CountryCode
import com.procurement.mdm.domain.model.code.LanguageCode
import com.procurement.mdm.domain.model.criterion.CriterionCategory
import com.procurement.mdm.model.record.StandardCriterionRecord

interface StandardCriterionRepository {

    fun findBy(country: CountryCode, language: LanguageCode): List<StandardCriterionRecord>

    fun findBy(
        country: CountryCode,
        language: LanguageCode,
        mainProcurementCategory: ProcurementCategory
    ): List<StandardCriterionRecord>

    fun findBy(
        country: CountryCode,
        language: LanguageCode,
        criteriaCategory: CriterionCategory
    ): List<StandardCriterionRecord>

    fun findBy(
        country: CountryCode,
        language: LanguageCode,
        mainProcurementCategory: ProcurementCategory,
        criteriaCategory: CriterionCategory
    ): List<StandardCriterionRecord>

}
