package com.procurement.mdm.repository

import com.procurement.mdm.model.entity.Country
import com.procurement.mdm.model.entity.Region
import com.procurement.mdm.model.entity.RegionKey
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
interface RegionRepository : JpaRepository<Region, RegionKey> {

    @Transactional(readOnly = true)
    fun findByRegionKeyCountry(country: Country): List<Region>

    @Transactional(readOnly = true)
    fun findByRegionKeyCodeAndRegionKeyCountry(code: String, country: Country): Region?
}
