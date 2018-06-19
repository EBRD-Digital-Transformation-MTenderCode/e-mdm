package com.procurement.mdm.service

import com.procurement.budget.model.bpe.ResponseDto
import com.procurement.mdm.repository.RegionRepository
import org.springframework.stereotype.Service

interface RegionService {

    fun getRegion(lang: String, country: String): ResponseDto
}

@Service
class RegionServiceImpl(private val regionRepository: RegionRepository) : RegionService {

    override fun getRegion(lang: String, country: String): ResponseDto {
        return ResponseDto(null, regionRepository.findByLanguageCodeAndCountryCode(lang, country))
    }
}
