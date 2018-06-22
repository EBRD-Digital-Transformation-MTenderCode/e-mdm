package com.procurement.mdm.service

import com.procurement.mdm.model.dto.ResponseDto
import com.procurement.mdm.model.dto.getResponseDto
import com.procurement.mdm.repository.BankRepository
import com.procurement.mdm.repository.GPAannexesRepository
import com.procurement.mdm.repository.HolidaysRepository
import org.springframework.stereotype.Service

interface AdditionalService {

    fun getBank(languageCode: String, countryCode: String, internal: Boolean): ResponseDto

    fun getGPAnnexes(languageCode: String, countryCode: String, internal: Boolean): ResponseDto

    fun getHolidays(languageCode: String, countryCode: String, internal: Boolean): ResponseDto
}

@Service
class AdditionalServiceImpl(private val bankRepository: BankRepository,
                            private val gpaAnnexesRepository: GPAannexesRepository,
                            private val holidaysRepository: HolidaysRepository,
                            private val validationService: ValidationService
) : AdditionalService {

    override fun getBank(languageCode: String, countryCode: String, internal: Boolean): ResponseDto {
        val countryId = validationService.getCountryId(
                languageCode = languageCode,
                countryCode = countryCode,
                internal = internal)
        return getResponseDto(
                default = null,
                items = bankRepository.findByCountryId(countryId),
                internal = internal)
    }

    override fun getGPAnnexes(languageCode: String, countryCode: String, internal: Boolean): ResponseDto {
        val countryId = validationService.getCountryId(
                languageCode = languageCode,
                countryCode = countryCode,
                internal = internal)
        return getResponseDto(
                default = null,
                items = gpaAnnexesRepository.findByCountry(countryId),
                internal = internal)
    }

    override fun getHolidays(languageCode: String, countryCode: String, internal: Boolean): ResponseDto {
        val countryId = validationService.getCountryId(
                languageCode = languageCode,
                countryCode = countryCode,
                internal = internal)
        return getResponseDto(
                default = null,
                items = holidaysRepository.findByCountryId(countryId),
                internal = internal)
    }


}
