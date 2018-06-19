package com.procurement.mdm.repository

import com.procurement.mdm.model.entity.Region
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
interface RegionRepository : JpaRepository<Region, String> {

    @Transactional(readOnly = true)
    fun findByLanguageCodeAndCountryCode(lang: String, country: String): List<Region>
}
