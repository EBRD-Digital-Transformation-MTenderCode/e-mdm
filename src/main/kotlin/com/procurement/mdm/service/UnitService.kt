package com.procurement.mdm.service

import com.procurement.mdm.model.dto.ResponseDto
import com.procurement.mdm.model.dto.getResponseDto
import com.procurement.mdm.model.entity.getItems
import com.procurement.mdm.repository.UnitRepository
import org.springframework.stereotype.Service

interface UnitService {

    fun getUnit(languageCode: String, unitClassCode: String, internal: Boolean): ResponseDto
}

@Service
class UnitServiceImpl(private val unitRepository: UnitRepository,
                      private val validationService: ValidationService) : UnitService {

    override fun getUnit(languageCode: String, unitClassCode: String, internal: Boolean): ResponseDto {
        validationService.checkLanguage(languageCode = languageCode, internal = internal)
        validationService.checkUnitClass(code = unitClassCode, internal = internal)
        val entities = unitRepository.findByUnitClassCodeAndUnitKeyLanguageCode(
                unitClassCode = unitClassCode,
                languageCode = languageCode)
        return getResponseDto(
                default = null,
                items = entities.getItems(),
                internal = internal)
    }
}
