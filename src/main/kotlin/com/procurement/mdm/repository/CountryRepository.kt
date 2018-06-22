package com.procurement.mdm.repository

import com.procurement.mdm.model.entity.Country
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
interface CountryRepository : JpaRepository<Country, String> {

    @Transactional(readOnly = true)
    fun findByLanguageId(languageId: String): List<Country>

    @Transactional(readOnly = true)
    fun findByCodeAndLanguageId(countryCode:String, languageId: String): Country?

}
