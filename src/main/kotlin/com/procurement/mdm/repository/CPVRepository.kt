package com.procurement.mdm.repository

import com.procurement.mdm.model.entity.CPV
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
interface CPVRepository : JpaRepository<CPV, String> {

    @Transactional(readOnly = true)
    fun findByLanguageIdAndParent(languageId: String, parentId: String = ""): List<CPV>

    @Transactional(readOnly = true)
    fun findByLanguageIdAndCode(languageId: String, code: String): CPV?
}
