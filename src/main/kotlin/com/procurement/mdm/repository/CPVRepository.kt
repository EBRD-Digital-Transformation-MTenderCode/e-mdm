package com.procurement.mdm.repository

import com.procurement.mdm.model.entity.CPV
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
interface CPVRepository : JpaRepository<CPV, String> {

    @Transactional(readOnly = true)
    fun findByLanguageCodeAndParent(lang: String, parent: String = ""): List<CPV>

    @Transactional(readOnly = true)
    fun findByCode(code: String): CPV?
}
