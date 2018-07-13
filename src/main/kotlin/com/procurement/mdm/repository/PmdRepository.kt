package com.procurement.mdm.repository

import com.procurement.mdm.model.entity.Country
import com.procurement.mdm.model.entity.Pmd
import com.procurement.mdm.model.entity.PmdKey
import com.procurement.mdm.model.entity.Region
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
interface PmdRepository : JpaRepository<Pmd, PmdKey> {

    @Transactional(readOnly = true)
    fun findByPmdKeyCountry(country: Country): List<Pmd>

    @Transactional(readOnly = true)
    fun findByPmdKeyCodeAndPmdKeyCountry(code: String, country: Country): Pmd?
}
