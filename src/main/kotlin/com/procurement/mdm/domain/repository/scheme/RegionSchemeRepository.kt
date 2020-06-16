package com.procurement.mdm.domain.repository.scheme

import com.procurement.mdm.domain.entity.RegionEntity
import com.procurement.mdm.domain.model.code.CountryCode
import com.procurement.mdm.domain.model.code.LanguageCode
import com.procurement.mdm.domain.model.code.RegionCode
import com.procurement.mdm.domain.model.scheme.RegionScheme

interface RegionSchemeRepository {

    fun findBy(region: RegionCode, scheme: RegionScheme, country: CountryCode, language: LanguageCode): RegionEntity?

    fun existsBy(scheme: RegionScheme): Boolean

    fun existsBy(region: RegionCode, scheme: RegionScheme): Boolean

    fun existsBy(region: RegionCode, scheme: RegionScheme, country: CountryCode): Boolean

    fun existsBy(region: RegionCode, country: CountryCode): Boolean
}
