package com.procurement.mdm.domain.repository.address

import com.procurement.mdm.domain.entity.LocalityEntity
import com.procurement.mdm.domain.model.code.CountryCode
import com.procurement.mdm.domain.model.code.LanguageCode
import com.procurement.mdm.domain.model.code.LocalityCode
import com.procurement.mdm.domain.model.code.RegionCode

interface AddressLocalityRepository {
    fun findBy(
        locality: LocalityCode,
        country: CountryCode,
        region: RegionCode,
        language: LanguageCode
    ): LocalityEntity?
}
