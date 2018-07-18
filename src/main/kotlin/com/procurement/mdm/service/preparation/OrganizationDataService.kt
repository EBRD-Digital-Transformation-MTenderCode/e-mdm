package com.procurement.mdm.service.preparation

import com.procurement.mdm.exception.ErrorType
import com.procurement.mdm.exception.InErrorException
import com.procurement.mdm.model.dto.data.OrganizationReference
import com.procurement.mdm.model.entity.Country
import com.procurement.mdm.repository.LocalityRepository
import com.procurement.mdm.repository.RegionRepository
import com.procurement.mdm.repository.RegistrationSchemeRepository
import org.springframework.stereotype.Service

interface OrganizationDataService {

    fun processOrganization(organization: OrganizationReference, country: Country)
}

@Service
class OrganizationDataServiceImpl(private val registrationSchemeRepository: RegistrationSchemeRepository,
                                  private val regionRepository: RegionRepository,
                                  private val localityRepository: LocalityRepository
) : OrganizationDataService {

    override fun processOrganization(organization: OrganizationReference, country: Country) {

        //registration scheme
        registrationSchemeRepository.findByRsKeyCodeAndRsKeyCountry(code = organization.identifier.id, country = country)
                ?: throw InErrorException(ErrorType.RS_UNKNOWN)

        val addressDetails = organization.address.addressDetails

        //country
        addressDetails.country.apply {
            scheme = country.scheme
            uri = country.uri
        }

        //region
        val regionEntity = regionRepository.findByRegionKeyCodeAndRegionKeyCountry(addressDetails.region.id, country)
                ?: throw InErrorException(ErrorType.REGION_UNKNOWN)
        addressDetails.region.apply {
            scheme = regionEntity.scheme
            uri = regionEntity.uri
        }

        //locality
        val localityEntity = localityRepository.findByLocalityKeyCodeAndLocalityKeyCountry(addressDetails.locality.id, country)
                ?: throw InErrorException(ErrorType.LOCALITY_UNKNOWN)
        addressDetails.locality.apply {
            scheme = localityEntity.scheme
            uri = localityEntity.uri
        }
    }
}

