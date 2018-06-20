package com.procurement.mdm.repository

import com.procurement.mdm.model.entity.CPVs
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
interface CPVsRepository : JpaRepository<CPVs, String> {

    @Transactional(readOnly = true)
    fun findByLanguageCodeAndParent(lang: String, parent: String = ""): List<CPVs>
}
