package com.procurement.mdm.service

import com.procurement.mdm.model.dto.ResponseDto
import com.procurement.mdm.model.dto.getResponseDto
import com.procurement.mdm.model.entity.getItems
import com.procurement.mdm.repository.RegionRepository
import org.springframework.stereotype.Service

interface RegionService {

    fun getRegion(languageCode: String, countryCode: String): ResponseDto
}

@Service
class RegionServiceImpl(private val regionRepository: RegionRepository,
                        private val validationService: ValidationService) : RegionService {

    override fun getRegion(languageCode: String, countryCode: String): ResponseDto {
        val country = validationService.getCountry(
                languageCode = languageCode,
                countryCode = countryCode)
        val entities = regionRepository.findByRegionKeyCountry(country)
        return getResponseDto(
                default = null,
                items = entities.getItems())
    }
}
