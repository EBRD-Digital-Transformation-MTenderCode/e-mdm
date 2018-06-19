package com.procurement.mdm.repository

import com.procurement.mdm.model.entity.Unit
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
interface UnitRepository : JpaRepository<Unit, String> {

    @Transactional(readOnly = true)
    fun findByLanguageCodeAndUnitClassCode(lang: String, unitClass: String): List<Unit>
}
