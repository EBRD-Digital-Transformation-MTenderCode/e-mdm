package com.procurement.mdm.repository

import com.procurement.mdm.model.entity.Units
import com.procurement.mdm.model.entity.UnitKey
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
interface UnitRepository : JpaRepository<Units, UnitKey> {

    @Transactional(readOnly = true)
    fun findByUnitClassCodeAndUnitKeyLanguageCode(unitClass: String? = "", languageCode: String): List<Units>

    @Transactional(readOnly = true)
    fun findByUnitKeyCodeAndUnitKeyLanguageCode(unitCode: String, languageCode: String): Units?

}
