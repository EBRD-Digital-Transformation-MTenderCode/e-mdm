package com.procurement.mdm.repository

import com.procurement.mdm.model.entity.EntityKind
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
interface EntityKindRepository : JpaRepository<EntityKind, String> {

    @Transactional(readOnly = true)
    fun findByCode(code: String): EntityKind?
}
