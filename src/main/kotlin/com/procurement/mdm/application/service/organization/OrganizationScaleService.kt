package com.procurement.mdm.application.service.organization

import com.procurement.mdm.application.exception.OrganizationScaleNotFoundException
import com.procurement.mdm.domain.model.code.CountryCode
import com.procurement.mdm.domain.repository.address.AddressCountryRepository
import com.procurement.mdm.domain.repository.organization.OrganizationScaleRepository
import org.springframework.stereotype.Service

interface OrganizationScaleService {
    fun findAllOnlyCode(country: String): List<String>
}

@Service
class OrganizationScaleServiceImpl(
    private val organizationScaleRepository: OrganizationScaleRepository,
    private val addressCountryRepository: AddressCountryRepository
) : OrganizationScaleService {

    override fun findAllOnlyCode(country: String): List<String> {
        val countryCode = CountryCode(country).apply {
            validation(addressCountryRepository)
        }

        val scalesCodes = organizationScaleRepository.findAllOnlyCode(country = countryCode)
        if (scalesCodes.isEmpty())
            throw OrganizationScaleNotFoundException(country = country)

        return scalesCodes
    }
}
