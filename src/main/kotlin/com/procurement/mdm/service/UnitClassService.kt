package com.procurement.mdm.service

import com.procurement.mdm.model.dto.ResponseDto
import com.procurement.mdm.model.dto.getResponseDto
import com.procurement.mdm.model.entity.getItems
import com.procurement.mdm.repository.UnitClassRepository
import org.springframework.stereotype.Service

interface UnitClassService {

    fun getUnitClassesByLanguage(languageCode: String, internal: Boolean): ResponseDto

}

@Service
class UnitClassServiceImpl(private val unitClassRepository: UnitClassRepository,
                           private val validationService: ValidationService) : UnitClassService {

    override fun getUnitClassesByLanguage(languageCode: String, internal: Boolean): ResponseDto {
        validationService.checkLanguage(languageCode, internal)
        val entities = unitClassRepository.findByUnitClassKeyLanguageCode(languageCode)
        return getResponseDto(
                default = null,
                items = entities.getItems(),
                internal = internal)
    }
}
