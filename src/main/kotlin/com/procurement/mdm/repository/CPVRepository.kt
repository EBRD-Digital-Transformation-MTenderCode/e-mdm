package com.procurement.mdm.repository

import com.procurement.mdm.model.entity.CPV
import com.procurement.mdm.model.entity.CpvIdentity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
interface CPVRepository : JpaRepository<CPV, CpvIdentity> {

    //    @Transactional(readOnly = true)
//    fun findByLanguageIdAndParent(languageId: String, parentId: String = ""): List<CPV>
//
//    @Transactional(readOnly = true)
//    fun findByLanguageIdAndCode(languageId: String, code: String): CPV?
    @Transactional(readOnly = true)
    fun findByParentAndCpvIdentityLanguageId(parentCode: String = "", languageId: String): List<CPV>
}
