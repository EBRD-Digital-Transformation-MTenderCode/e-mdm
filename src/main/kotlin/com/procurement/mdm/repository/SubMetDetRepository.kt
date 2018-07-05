package com.procurement.mdm.repository

import com.procurement.mdm.model.entity.SubMetDet
import com.procurement.mdm.model.entity.SubMetDetKey
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
interface SubMetDetRepository : JpaRepository<SubMetDet, SubMetDetKey> {

    @Transactional(readOnly = true)
    fun findBySmdKeyLanguageCode(languageCode: String): List<SubMetDet>
}
