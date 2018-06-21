package com.procurement.mdm.service

import com.procurement.mdm.model.dto.ResponseDto
import com.procurement.mdm.model.dto.getResponseDto
import com.procurement.mdm.repository.UnitRepository
import org.springframework.stereotype.Service

interface UnitService {

    fun getUnit(lang: String, unitClass: String, internal: Boolean): ResponseDto
}

@Service
class UnitServiceImpl(private val unitRepository: UnitRepository) : UnitService {

    override fun getUnit(lang: String, unitClass: String, internal: Boolean): ResponseDto {
        return getResponseDto(
                default = null,
                items = unitRepository.findByLanguageCodeAndUnitClassCode(lang, unitClass),
                internal = internal)
    }
}
