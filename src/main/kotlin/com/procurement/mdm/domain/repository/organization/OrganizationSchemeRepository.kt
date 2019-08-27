package com.procurement.mdm.domain.repository.organization

import com.procurement.mdm.domain.model.code.CountryCode

interface OrganizationSchemeRepository {
    fun findAllOnlyCode(country: CountryCode): List<String>
}
