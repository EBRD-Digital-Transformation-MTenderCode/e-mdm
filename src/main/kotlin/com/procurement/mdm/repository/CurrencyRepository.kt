package com.procurement.mdm.repository

import com.procurement.mdm.model.entity.Currency
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
interface CurrencyRepository : JpaRepository<Currency, String> {

    @Transactional(readOnly = true)
    fun findByLanguageCodeAndCountriesCode(lang: String, country: String): List<Currency>
}
