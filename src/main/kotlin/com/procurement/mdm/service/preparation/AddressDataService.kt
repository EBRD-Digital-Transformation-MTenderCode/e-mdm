package com.procurement.mdm.service.preparation

import com.procurement.mdm.exception.ErrorType
import com.procurement.mdm.exception.InErrorException
import com.procurement.mdm.model.dto.data.Address
import com.procurement.mdm.model.entity.Country
import com.procurement.mdm.repository.LocalityRepository
import com.procurement.mdm.repository.RegionRepository
import org.springframework.stereotype.Service

interface AddressDataService {

    fun processAddress(address: Address, country: Country)
}

@Service
class AddressDataServiceImpl(private val regionRepository: RegionRepository,
                             private val localityRepository: LocalityRepository) : AddressDataService {

    override fun processAddress(address: Address, country: Country) {

        val addressDetails = address.addressDetails
        //country

        if(addressDetails.country.id.isBlank()) throw InErrorException(ErrorType.COUNTRY_NOT_FOUND)
        if (addressDetails.country.id != country.countryKey?.code) throw InErrorException(ErrorType.INVALID_COUNTRY)
        addressDetails.country.apply {
            scheme = country.scheme
            description = country.name
            uri = country.uri
        }
        //region
        val regionEntity = regionRepository.findByRegionKeyCodeAndRegionKeyCountry(addressDetails.region.id, country)
                ?: throw InErrorException(ErrorType.REGION_UNKNOWN)
        addressDetails.region.apply {
            scheme = regionEntity.scheme
            description = regionEntity.name
            uri = regionEntity.uri
        }
        //locality
        val schemeEntity = localityRepository.findOneByScheme(addressDetails.locality.scheme)
        if (schemeEntity != null) {
            val localityEntity = localityRepository.findByLocalityKeyCodeAndLocalityKeyRegionAndScheme(
                    addressDetails.locality.id,
                    regionEntity,
                    addressDetails.locality.scheme)
                    ?: throw InErrorException(ErrorType.LOCALITY_UNKNOWN)
            addressDetails.locality.apply {
                description = localityEntity.name
                uri = localityEntity.uri
            }
        } else {
            addressDetails.locality.uri = null
        }
    }
}

