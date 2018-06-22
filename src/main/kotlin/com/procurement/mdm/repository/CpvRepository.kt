package com.procurement.mdm.repository

import com.procurement.mdm.model.entity.Cpv
import com.procurement.mdm.model.entity.CpvKey
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
interface CpvRepository : JpaRepository<Cpv, CpvKey> {

    @Transactional(readOnly = true)
    fun findByCpvKeyCodeAndCpvKeyLanguageCode(code: String, languageCode: String): Cpv?

    @Transactional(readOnly = true)
    fun findByParentAndCpvKeyLanguageCode(parentCode: String = "", languageCode: String): List<Cpv>
}
