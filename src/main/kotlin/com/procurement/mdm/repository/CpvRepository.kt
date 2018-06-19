package com.procurement.mdm.repository

import com.procurement.mdm.model.entity.Cpv
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CpvRepository : JpaRepository<Cpv, String> {

    fun findCpvsByLanguageCode(lang: String): List<Cpv>

}
