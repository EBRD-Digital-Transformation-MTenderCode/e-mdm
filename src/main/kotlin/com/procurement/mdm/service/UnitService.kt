package com.procurement.mdm.service

import com.procurement.mdm.model.dto.ResponseDto
import com.procurement.mdm.model.dto.getResponseDto
import com.procurement.mdm.model.entity.getItems
import com.procurement.mdm.repository.UnitRepository
import org.springframework.stereotype.Service

interface UnitService {

    fun getUnit(languageCode: String, unitClassCode: String): ResponseDto
}

@Service
class UnitServiceImpl(private val unitRepository: UnitRepository,
                      private val validationService: ValidationService) : UnitService {

    override fun getUnit(languageCode: String, unitClassCode: String): ResponseDto {
        validationService.checkLanguage(languageCode = languageCode)
        val unitClass = validationService.getUnitClass(
                languageCode = languageCode,
                code = unitClassCode)
        val entities = unitRepository.findByUnitKeyUnitClass(unitClass)
        return getResponseDto(
                default = null,
                items = entities.getItems())
    }
}
