package com.procurement.mdm.domain.repository.scheme

import com.procurement.mdm.domain.entity.CountryEntity
import com.procurement.mdm.domain.model.code.CountryCode
import com.procurement.mdm.domain.model.code.LanguageCode
import com.procurement.mdm.domain.model.scheme.CountryScheme

interface CountrySchemeRepository {
    fun existsBy(scheme: CountryScheme): Boolean

    fun existsBy(scheme: CountryScheme, country: CountryCode): Boolean

    fun findBy(scheme: CountryScheme, country: CountryCode, language: LanguageCode): CountryEntity?
}
