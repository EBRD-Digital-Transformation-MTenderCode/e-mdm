package com.procurement.mdm.service

import com.procurement.mdm.model.bpe.ResponseDto
import com.procurement.mdm.repository.UnitRepository
import org.springframework.stereotype.Service

interface UnitService {

    fun getUnit(lang: String, unitClass: String): ResponseDto
}

@Service
class UnitServiceImpl(private val unitRepository: UnitRepository) : UnitService {

    override fun getUnit(lang: String, unitClass: String): ResponseDto {
        return ResponseDto(null, unitRepository.findByLanguageCodeAndUnitClassCode(lang, unitClass))
    }
}
