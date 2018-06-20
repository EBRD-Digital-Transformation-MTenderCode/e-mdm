package com.procurement.mdm.service

import com.procurement.mdm.model.bpe.ResponseDto
import com.procurement.mdm.repository.RegistrationSchemeRepository
import org.springframework.stereotype.Service

interface RegistrationSchemeService {

    fun getRegistrationScheme(lang: String, country: String): ResponseDto
}

@Service
class RegistrationSchemeServiceImpl(
        private val registrationSchemeRepository: RegistrationSchemeRepository) : RegistrationSchemeService {

    override fun getRegistrationScheme(lang: String, country: String): ResponseDto {
        return ResponseDto(null,
                registrationSchemeRepository.findByLanguageCodeAndCountryCode(lang, country))
    }
}
