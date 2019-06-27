package com.procurement.mdm.domain.repository.address

import com.procurement.mdm.domain.entity.RegionEntity
import com.procurement.mdm.domain.model.code.CountryCode
import com.procurement.mdm.domain.model.code.LanguageCode
import com.procurement.mdm.domain.model.code.RegionCode

interface AddressRegionRepository {
    fun findBy(region: RegionCode, country: CountryCode, language: LanguageCode): RegionEntity?
}
