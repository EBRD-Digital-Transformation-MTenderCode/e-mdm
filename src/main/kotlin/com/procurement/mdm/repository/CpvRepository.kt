package com.procurement.mdm.repository

import com.procurement.mdm.model.entity.Cpv
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CpvRepository : JpaRepository<Cpv, String> {

    fun findCpvsByLanguage(languageCode: String): List<Cpv>

//    fun findCpvsByLanguage_Iso6391AndGroupAndParent(languageCode: String, group: Int?, parent: String): List<Cpv>
//
//    fun findCpvsByLanguage_Iso6391AndGroup(languageCode: String, group: Int?): List<Cpv>
//
//    fun findCpvsByLanguage_Iso6391AndParent(languageCode: String, parent: String): List<Cpv>
}
