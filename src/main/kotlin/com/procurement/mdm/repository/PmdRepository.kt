package com.procurement.mdm.repository

import com.procurement.mdm.model.entity.Pmd
import com.procurement.mdm.model.entity.PmdKey
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
interface PmdRepository : JpaRepository<Pmd, PmdKey> {

    @Transactional(readOnly = true)
    fun findByPmdKeyLanguageCode(languageCode: String): List<Pmd>
}
