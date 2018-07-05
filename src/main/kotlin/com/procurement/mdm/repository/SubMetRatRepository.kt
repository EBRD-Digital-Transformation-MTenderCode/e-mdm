package com.procurement.mdm.repository

import com.procurement.mdm.model.entity.SubMetRat
import com.procurement.mdm.model.entity.SubMetRatKey
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
interface SubMetRatRepository : JpaRepository<SubMetRat, SubMetRatKey> {

    @Transactional(readOnly = true)
    fun findBySmrKeyLanguageCode(languageCode: String): List<SubMetRat>
}
