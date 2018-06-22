package com.procurement.mdm.service

import com.procurement.mdm.model.dto.ResponseDto
import com.procurement.mdm.model.dto.getResponseDto
import com.procurement.mdm.repository.RegistrationSchemeRepository
import org.springframework.stereotype.Service

interface RegistrationSchemeService {

    fun getRegistrationScheme(languageCode: String, countryCode: String, internal: Boolean): ResponseDto
}

@Service
class RegistrationSchemeServiceImpl(
        private val registrationSchemeRepository: RegistrationSchemeRepository,
        private val validationService: ValidationService) : RegistrationSchemeService {

    override fun getRegistrationScheme(languageCode: String, countryCode: String, internal: Boolean): ResponseDto {
        val countryId = validationService.getCountryId(
                languageCode = languageCode,
                countryCode = countryCode,
                internal = internal)
        return getResponseDto(
                default = null,
                items = registrationSchemeRepository.findByCountryId(countryId),
                internal = internal)
    }
}
