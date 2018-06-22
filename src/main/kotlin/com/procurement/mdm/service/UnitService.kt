//package com.procurement.mdm.service
//
//import com.procurement.mdm.model.dto.ResponseDto
//import com.procurement.mdm.model.dto.getResponseDto
//import com.procurement.mdm.repository.UnitRepository
//import org.springframework.stereotype.Service
//
//interface UnitService {
//
//    fun getUnit(languageCode: String, unitClassCode: String, internal: Boolean): ResponseDto
//}
//
//@Service
//class UnitServiceImpl(private val unitRepository: UnitRepository,
//                      private val validationService: ValidationService) : UnitService {
//
//    override fun getUnit(languageCode: String, unitClassCode: String, internal: Boolean): ResponseDto {
//        val languageId = validationService.checkLanguage(
//                languageCode = languageCode,
//                internal = internal)
//        val unitClassId = validationService.getUnitClassId(
//                code = unitClassCode,
//                internal = internal)
//        return getResponseDto(
//                default = null,
//                items = unitRepository.findByLanguageIdAndUnitClassId(languageId, unitClassId),
//                internal = internal)
//    }
//}
