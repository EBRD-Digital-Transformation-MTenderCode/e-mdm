package com.procurement.mdm.service

import com.procurement.mdm.model.dto.ResponseDto
import com.procurement.mdm.model.dto.getResponseDto
import com.procurement.mdm.model.entity.getItems
import com.procurement.mdm.repository.RegistrationSchemeRepository
import org.springframework.stereotype.Service

interface RegistrationSchemeService {

    fun getRegistrationScheme(languageCode: String, countryCode: String): ResponseDto
}

@Service
class RegistrationSchemeServiceImpl(
        private val registrationSchemeRepository: RegistrationSchemeRepository,
        private val validationService: ValidationService) : RegistrationSchemeService {

    override fun getRegistrationScheme(languageCode: String, countryCode: String): ResponseDto {
        val country = validationService.getCountry(
                languageCode = languageCode,
                countryCode = countryCode)
        val entities = registrationSchemeRepository.findByRsKeyCountry(country)
        return getResponseDto(items = entities.getItems())
    }
}
