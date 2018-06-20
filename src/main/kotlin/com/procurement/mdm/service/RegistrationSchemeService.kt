package com.procurement.mdm.service

import com.procurement.mdm.model.dto.ResponseDto
import com.procurement.mdm.model.dto.getResponseDto
import com.procurement.mdm.repository.RegistrationSchemeRepository
import org.springframework.stereotype.Service

interface RegistrationSchemeService {

    fun getRegistrationScheme(lang: String, country: String, internal: Boolean): ResponseDto
}

@Service
class RegistrationSchemeServiceImpl(
        private val registrationSchemeRepository: RegistrationSchemeRepository) : RegistrationSchemeService {

    override fun getRegistrationScheme(lang: String, country: String, internal: Boolean): ResponseDto {
        return getResponseDto(
                default = null,
                items = registrationSchemeRepository.findByLanguageCodeAndCountryCode(lang, country),
                internal = internal)
    }
}
