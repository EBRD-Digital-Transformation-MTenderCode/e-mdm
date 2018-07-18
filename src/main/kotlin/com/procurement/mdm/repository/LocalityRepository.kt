package com.procurement.mdm.repository

import com.procurement.mdm.model.entity.Country
import com.procurement.mdm.model.entity.Locality
import com.procurement.mdm.model.entity.LocalityKey
import com.procurement.mdm.model.entity.Region
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
interface LocalityRepository : JpaRepository<Locality, LocalityKey> {

    @Transactional(readOnly = true)
    fun findByLocalityKeyCountry(country: Country): List<Locality>

    @Transactional(readOnly = true)
    fun findByLocalityKeyCodeAndLocalityKeyCountry(code: String, country: Country): Locality?
}
