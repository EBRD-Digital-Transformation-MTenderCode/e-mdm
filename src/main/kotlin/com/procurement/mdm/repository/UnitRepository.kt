package com.procurement.mdm.repository

import com.procurement.mdm.model.entity.Unit
import com.procurement.mdm.model.entity.UnitKey
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
interface UnitRepository : JpaRepository<Unit, UnitKey> {

    @Transactional(readOnly = true)
    fun findByUnitClassCodeAndUnitKeyLanguageCode(unitClassCode: String, languageCode: String): List<Unit>
}
