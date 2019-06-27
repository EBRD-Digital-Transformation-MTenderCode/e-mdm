package com.procurement.mdm.domain.repository.organization

import com.procurement.mdm.domain.model.code.CountryCode

interface OrganizationScaleRepository {
    fun findAllOnlyCode(country: CountryCode): List<String>
}
