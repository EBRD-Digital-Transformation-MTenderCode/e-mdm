//package com.procurement.mdm.service
//
//import com.procurement.mdm.model.dto.ResponseDto
//import com.procurement.mdm.model.dto.getResponseDto
//import com.procurement.mdm.repository.RegionRepository
//import org.springframework.stereotype.Service
//
//interface RegionService {
//
//    fun getRegion(languageCode: String, countryCode: String, internal: Boolean): ResponseDto
//}
//
//@Service
//class RegionServiceImpl(private val regionRepository: RegionRepository,
//                        private val validationService: ValidationService) : RegionService {
//
//    override fun getRegion(languageCode: String, countryCode: String, internal: Boolean): ResponseDto {
//        val countryId = validationService.getCountryId(
//                languageCode = languageCode,
//                countryCode = countryCode,
//                internal = internal)
//        return getResponseDto(
//                default = null,
//                items = regionRepository.findByCountryId(countryId),
//                internal = internal)
//    }
//}
