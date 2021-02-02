package com.procurement.mdm.application.service.organization

import com.procurement.mdm.application.exception.OrganizationSchemeNotFoundException
import com.procurement.mdm.domain.model.code.CountryCode
import com.procurement.mdm.domain.repository.address.AddressCountryRepository
import com.procurement.mdm.domain.repository.organization.OrganizationSchemeRepository
import org.springframework.stereotype.Service

interface OrganizationSchemeService {
    fun find(countries: List<String>): Map<CountryCode, List<String>>
}

@Service
class OrganizationSchemeServiceImpl(
    private val organizationSchemeRepository: OrganizationSchemeRepository,
    private val addressCountryRepository: AddressCountryRepository
) : OrganizationSchemeService {

    override fun find(countries: List<String>): Map<CountryCode, List<String>> {
        val codes = countries.map { country ->
            getCountryCode(country)
        }

        return codes.asSequence()
            .map { code ->
                val schemes = getSchemesByCountry(code = code)
                code to schemes
            }
            .toMap()
    }

    private fun getSchemesByCountry(code: CountryCode): List<String> {
        return organizationSchemeRepository.find(country = code)
            .also {
                if (it.isEmpty())
                    throw OrganizationSchemeNotFoundException(country = code.value)
            }
    }

    private fun getCountryCode(country: String) = CountryCode(country)
        .also {
            it.validation(addressCountryRepository)
        }
}
