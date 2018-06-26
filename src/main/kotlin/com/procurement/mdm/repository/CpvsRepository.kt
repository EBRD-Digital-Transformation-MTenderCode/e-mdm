package com.procurement.mdm.repository

import com.procurement.mdm.model.entity.Cpvs
import com.procurement.mdm.model.entity.CpvsKey
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
interface CpvsRepository : JpaRepository<Cpvs, CpvsKey> {

    @Transactional(readOnly = true)
    fun findByCpvsKeyLanguageCode(languageCode: String): List<Cpvs>
}
