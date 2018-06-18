package com.procurement.mdm.repository

import com.procurement.mdm.model.entity.Country
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CountryRepository : JpaRepository<Country, Long> {

    fun findCountriesByCode(code: String): List<Country>

    fun findCountriesByName(name: String): List<Country>
}
