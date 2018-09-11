package com.procurement.mdm.service

import com.procurement.mdm.exception.ErrorType
import com.procurement.mdm.exception.ExErrorException
import com.procurement.mdm.model.dto.ResponseDto
import com.procurement.mdm.model.dto.getResponseDto
import com.procurement.mdm.model.entity.getItems
import com.procurement.mdm.repository.UnitClassRepository
import com.procurement.mdm.repository.UnitRepository
import org.springframework.stereotype.Service

interface UnitClassService {

    fun getUnitClassesByLanguage(languageCode: String): ResponseDto

    fun getUnitClassByUnit(languageCode: String, unitCode: String): ResponseDto
}

@Service
class UnitClassServiceImpl(private val unitClassRepository: UnitClassRepository,
                           private val unitRepository: UnitRepository,
                           private val validationService: ValidationService) : UnitClassService {

    override fun getUnitClassesByLanguage(languageCode: String): ResponseDto {
        validationService.getLanguage(languageCode)
        val entities = unitClassRepository.findByUnitClassKeyLanguageCode(languageCode)
        return getResponseDto(items = entities.getItems())
    }

    override fun getUnitClassByUnit(languageCode: String, unitCode: String): ResponseDto {
        validationService.getLanguage(languageCode)
        val unitEntity = unitRepository.findByUnitKeyCodeAndUnitKeyLanguageCode(unitCode, languageCode)
                ?: throw ExErrorException(ErrorType.UNIT_UNKNOWN)
        val unitClassEntity = unitClassRepository.findByUnitClassKeyCodeAndUnitClassKeyLanguageCode(unitEntity.unitClassCode, languageCode)
                ?: throw ExErrorException(ErrorType.UNIT_CLASS_UNKNOWN)
        return getResponseDto(default = unitClassEntity.name, items = listOf(unitClassEntity).getItems())
    }

}
