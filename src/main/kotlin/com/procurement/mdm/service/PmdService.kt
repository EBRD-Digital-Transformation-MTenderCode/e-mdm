package com.procurement.mdm.service

import com.procurement.mdm.model.dto.ResponseDto
import com.procurement.mdm.model.dto.getResponseDto
import com.procurement.mdm.model.entity.getItems
import com.procurement.mdm.repository.PmdRepository
import org.springframework.stereotype.Service

interface PmdService {

    fun getPmd(languageCode: String, countryCode: String): ResponseDto
}

@Service
class PmdServiceImpl(private val pmdRepository: PmdRepository,
                     private val validationService: ValidationService) : PmdService {

    override fun getPmd(languageCode: String, countryCode: String): ResponseDto {
        val country = validationService.getCountry(
                languageCode = languageCode,
                countryCode = countryCode)
        val entities = pmdRepository.findByPmdKeyCountry(country)
        return getResponseDto(items = entities.getItems())
    }
}

