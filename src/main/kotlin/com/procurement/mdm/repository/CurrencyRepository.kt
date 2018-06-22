package com.procurement.mdm.repository

import com.procurement.mdm.model.entity.Country
import com.procurement.mdm.model.entity.Currency
import com.procurement.mdm.model.entity.CurrencyKey
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
interface CurrencyRepository : JpaRepository<Currency, CurrencyKey> {

    @Transactional(readOnly = true)
    fun findByCurrencyKeyLanguageCodeAndCountries(languageCode: String, country: Country): List<Currency>
}
