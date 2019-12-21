package com.procurement.mdm.application.service.organization

import com.procurement.mdm.application.exception.OrganizationSchemeNotFoundException
import com.procurement.mdm.domain.model.code.CountryCode
import com.procurement.mdm.domain.repository.address.AddressCountryRepository
import com.procurement.mdm.domain.repository.organization.OrganizationSchemeRepository
import org.springframework.stereotype.Service

interface OrganizationSchemeService {
    fun find(country: String): List<String>
}

@Service
class OrganizationSchemeServiceImpl(
    private val organizationSchemeRepository: OrganizationSchemeRepository,
    private val addressCountryRepository: AddressCountryRepository
) : OrganizationSchemeService {

    override fun find(country: String): List<String> {
        val countryCode = CountryCode(country)
            .apply {
                validation(addressCountryRepository)
            }

        val schemesCodes = organizationSchemeRepository.find(country = countryCode)
        if (schemesCodes.isEmpty())
            throw OrganizationSchemeNotFoundException(country = countryCode.value)

        return schemesCodes
    }
}
