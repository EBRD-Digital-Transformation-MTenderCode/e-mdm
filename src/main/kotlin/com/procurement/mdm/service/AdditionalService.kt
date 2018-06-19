package com.procurement.mdm.service

import com.procurement.budget.model.bpe.ResponseDto
import com.procurement.mdm.repository.BankRepository
import com.procurement.mdm.repository.GPAannexesRepository
import com.procurement.mdm.repository.HolidaysRepository
import com.procurement.mdm.repository.RegionRepository
import org.springframework.stereotype.Service

interface AdditionalService {

    fun getBank(lang: String, country: String): ResponseDto

    fun getGPAnnexes(lang: String, country: String): ResponseDto

    fun getHolidays(lang: String, country: String): ResponseDto
}

@Service
class AdditionalServiceImpl(private val bankRepository: BankRepository,
                            private val gpaAnnexesRepository: GPAannexesRepository,
                            private val holidaysRepository: HolidaysRepository
) : AdditionalService {

    override fun getBank(lang: String, country: String): ResponseDto {
        return ResponseDto(null, bankRepository.findByLanguageCodeAndCountryCode(lang, country))
    }

    override fun getGPAnnexes(lang: String, country: String): ResponseDto {
        return ResponseDto(null, gpaAnnexesRepository.findByLanguageCodeAndCountryCode(lang, country))
    }

    override fun getHolidays(lang: String, country: String): ResponseDto {
        return ResponseDto(null, holidaysRepository.findByLanguageCodeAndCountryCode(lang, country))
    }
}
