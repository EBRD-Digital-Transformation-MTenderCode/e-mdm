package com.procurement.mdm.service

import com.procurement.mdm.model.dto.ResponseDto
import com.procurement.mdm.model.dto.getResponseDto
import com.procurement.mdm.model.entity.getItems
import com.procurement.mdm.repository.LocalityRepository
import org.springframework.stereotype.Service

interface LocalityService {

    fun getLocality(languageCode: String, countryCode: String, regionCode: String): ResponseDto
}

@Service
class LocalityServiceImpl(private val localityRepository: LocalityRepository,
                          private val validationService: ValidationService) : LocalityService {

    override fun getLocality(languageCode: String, countryCode: String, regionCode: String): ResponseDto {
        val region = validationService.getRegion(
                languageCode = languageCode,
                countryCode = countryCode,
                regionCode = regionCode)
        val entities = localityRepository.findByLocalityKeyRegion(region)
        return getResponseDto(items = entities.getItems())
    }
}
