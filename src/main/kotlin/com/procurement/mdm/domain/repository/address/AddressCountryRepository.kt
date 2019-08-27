package com.procurement.mdm.domain.repository.address

import com.procurement.mdm.domain.entity.CountryEntity
import com.procurement.mdm.domain.model.code.CountryCode
import com.procurement.mdm.domain.model.code.LanguageCode

interface AddressCountryRepository {
    fun exists(code: CountryCode): Boolean

    fun findAll(language: LanguageCode): List<CountryEntity>

    fun findBy(country: CountryCode, language: LanguageCode): CountryEntity?
}
