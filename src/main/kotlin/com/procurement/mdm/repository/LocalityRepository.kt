package com.procurement.mdm.repository

import com.procurement.mdm.model.entity.Locality
import com.procurement.mdm.model.entity.LocalityKey
import com.procurement.mdm.model.entity.Region
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
interface LocalityRepository : JpaRepository<Locality, LocalityKey> {

    @Transactional(readOnly = true)
    fun findByLocalityKeyRegion(region: Region): List<Locality>

    @Transactional(readOnly = true)
    @Query(value = "SELECT * FROM locality WHERE locality.scheme = ?1 LIMIT 1", nativeQuery = true)
    fun findOneByScheme(scheme: String): Locality?

    @Transactional(readOnly = true)
    fun findByLocalityKeyCodeAndLocalityKeyRegionAndScheme(code: String, region: Region, scheme: String): Locality?
}
