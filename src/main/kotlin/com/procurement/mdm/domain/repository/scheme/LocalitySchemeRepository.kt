package com.procurement.mdm.domain.repository.scheme

import com.procurement.mdm.domain.entity.LocalityEntity
import com.procurement.mdm.domain.model.code.LanguageCode
import com.procurement.mdm.domain.model.code.LocalityCode
import com.procurement.mdm.domain.model.code.RegionCode
import com.procurement.mdm.domain.model.scheme.LocalityScheme

interface LocalitySchemeRepository {

    fun findBy(scheme: LocalityScheme, locality: LocalityCode, region: RegionCode, language: LanguageCode): LocalityEntity?

    fun existsBy(scheme: LocalityScheme): Boolean

    fun existsBy(scheme: LocalityScheme, locality: LocalityCode): Boolean

    fun existsBy(scheme: LocalityScheme, locality: LocalityCode, region: RegionCode): Boolean
}
