package com.procurement.mdm.service

import com.procurement.mdm.model.dto.ResponseDto
import com.procurement.mdm.model.dto.getResponseDto
import com.procurement.mdm.model.entity.getItems
import com.procurement.mdm.repository.PmdRepository
import com.procurement.mdm.repository.SubMetDetRepository
import com.procurement.mdm.repository.SubMetRatRepository
import org.springframework.stereotype.Service

interface SubMetDetService {

    fun getSubMetDet(languageCode: String, internal: Boolean): ResponseDto
}

@Service
class SubMetDetServiceImpl(private val subMetDetRepository: SubMetDetRepository,
                           private val validationService: ValidationService) : SubMetDetService {

    override fun getSubMetDet(languageCode: String, internal: Boolean): ResponseDto {
        validationService.checkLanguage(languageCode, internal)
        val entities = subMetDetRepository.findBySubMetDetKeyLanguageCode(languageCode = languageCode)
        return getResponseDto(
            default = null,
            items = entities.getItems(),
            internal = internal)
    }
}

