//package com.procurement.mdm.service
//
//import com.procurement.mdm.model.dto.ResponseDto
//import com.procurement.mdm.model.dto.getResponseDto
//import com.procurement.mdm.repository.LanguageRepository
//import org.springframework.stereotype.Service
//
//interface LanguageService {
//
//    fun getLanguages(internal: Boolean): ResponseDto
//
//}
//
//@Service
//class LanguageServiceImpl(private val languageRepository: LanguageRepository) : LanguageService {
//
//    override fun getLanguages(internal: Boolean): ResponseDto {
//        return getResponseDto(
//                default = null,
//                items = languageRepository.findAll(),
//                internal = internal)
//    }
//}
