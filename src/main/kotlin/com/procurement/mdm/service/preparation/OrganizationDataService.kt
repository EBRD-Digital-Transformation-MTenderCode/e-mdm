package com.procurement.mdm.service.preparation

import com.procurement.mdm.exception.ErrorType
import com.procurement.mdm.exception.InErrorException
import com.procurement.mdm.model.dto.data.OrganizationReference
import com.procurement.mdm.model.entity.Country
import com.procurement.mdm.repository.RegistrationSchemeRepository
import org.springframework.stereotype.Service

interface OrganizationDataService {

    fun processOrganization(organization: OrganizationReference, country: Country)
}

@Service
class OrganizationDataServiceImpl(private val registrationSchemeRepository: RegistrationSchemeRepository,
                                  private val addressDataService: AddressDataService) : OrganizationDataService {

    override fun processOrganization(organization: OrganizationReference, country: Country) {
        registrationSchemeRepository.findByRsKeyCodeAndRsKeyCountry(code = organization.identifier.scheme, country = country)
                ?: throw InErrorException(ErrorType.RS_UNKNOWN)

        addressDataService.processAddress(organization.address, country)
    }
}

