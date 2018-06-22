package com.procurement.mdm.repository

import com.procurement.mdm.model.entity.CPV
import com.procurement.mdm.model.entity.CpvKey
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
interface CPVRepository : JpaRepository<CPV, CpvKey> {

    @Transactional(readOnly = true)
    fun findByCpvKeyCodeAndCpvKeyLanguageCode(code: String, languageCode: String): CPV?

    @Transactional(readOnly = true)
    fun findByParentAndCpvKeyLanguageCode(parentCode: String = "", languageCode: String): List<CPV>
}
