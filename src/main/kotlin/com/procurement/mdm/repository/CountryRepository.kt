package com.procurement.mdm.repository

import com.procurement.mdm.model.entity.Country
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
interface CountryRepository : JpaRepository<Country, String> {

    @Transactional(readOnly = true)
    fun findByCountryKeyLanguageCode(languageCode: String): List<Country>

    @Transactional(readOnly = true)
    fun findByCountryKeyLanguageCodeAndCountryKeyCode(languageCode: String, code: String): Country?

    @Transactional(readOnly = true)
    fun findByCountryKeyLanguageCodeAndName(languageCode: String, name: String): Country?

}
