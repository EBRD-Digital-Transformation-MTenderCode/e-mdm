package com.procurement.mdm.service

import com.procurement.mdm.model.dto.ResponseDto
import com.procurement.mdm.model.dto.getResponseDto
import com.procurement.mdm.repository.RegionRepository
import org.springframework.stereotype.Service

interface RegionService {

    fun getRegion(lang: String, country: String, internal: Boolean): ResponseDto
}

@Service
class RegionServiceImpl(private val regionRepository: RegionRepository) : RegionService {

    override fun getRegion(lang: String, country: String, internal: Boolean): ResponseDto {
        return getResponseDto(
                default = null,
                items = regionRepository.findByCountry(country),
                internal = internal)
    }
}
