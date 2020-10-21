package com.procurement.mdm.repository

import com.procurement.mdm.model.entity.Cpv
import com.procurement.mdm.model.entity.CpvKey
import com.procurement.mdm.model.entity.Language
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
interface CpvRepository : JpaRepository<Cpv, CpvKey> {

    @Transactional(readOnly = true)
    fun findByCpvKeyCodeAndCpvKeyLanguageCode(code: String, languageCode: String): Cpv?

    @Transactional(readOnly = true)
    @Query("SELECT c FROM Cpv c WHERE c.cpvKey.code LIKE ?1% AND c.cpvKey.language = ?2")
    fun getCommonClass(code: String, language: Language): Cpv?

    @Transactional(readOnly = true)
    fun findByParentAndCpvKeyLanguageCode(parentCode: String = "", languageCode: String): List<Cpv>

    @Transactional(readOnly = true)
    fun findByCpvKeyCode(code: String): List<Cpv>
}
