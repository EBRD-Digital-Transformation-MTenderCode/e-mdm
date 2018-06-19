package com.procurement.mdm.repository

import com.procurement.mdm.model.entity.Country
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CountryRepository : JpaRepository<Country, String> {

    fun findCountriesByLanguageCode(lang: String): List<Country>

    fun findCountriesByLanguageCodeAndDefault(lang: String, def:Boolean = true): List<Country>
}
