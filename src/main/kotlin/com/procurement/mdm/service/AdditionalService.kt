package com.procurement.mdm.service

import com.procurement.mdm.model.dto.ResponseDto
import com.procurement.mdm.model.dto.getResponseDto
import com.procurement.mdm.repository.BankRepository
import com.procurement.mdm.repository.GPAannexesRepository
import com.procurement.mdm.repository.HolidaysRepository
import org.springframework.stereotype.Service

interface AdditionalService {

    fun getBank(lang: String, country: String, internal: Boolean): ResponseDto

    fun getGPAnnexes(lang: String, country: String, internal: Boolean): ResponseDto

    fun getHolidays(lang: String, country: String, internal: Boolean): ResponseDto
}

@Service
class AdditionalServiceImpl(private val bankRepository: BankRepository,
                            private val gpaAnnexesRepository: GPAannexesRepository,
                            private val holidaysRepository: HolidaysRepository
) : AdditionalService {

    override fun getBank(lang: String, country: String, internal: Boolean): ResponseDto {
        return getResponseDto(
                default = null,
                items = bankRepository.findByCountry(country),
                internal = internal)
    }

    override fun getGPAnnexes(lang: String, country: String, internal: Boolean): ResponseDto {
        return getResponseDto(
                default = null,
                items = gpaAnnexesRepository.findByCountry(country),
                internal = internal)
    }

    override fun getHolidays(lang: String, country: String, internal: Boolean): ResponseDto {
        return getResponseDto(
                default = null,
                items = holidaysRepository.findByCountry(country),
                internal = internal)
    }


}
