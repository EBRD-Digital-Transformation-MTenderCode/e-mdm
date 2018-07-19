package com.procurement.mdm.service.preparation

import com.procurement.access.utils.toObject
import com.procurement.mdm.exception.ErrorType
import com.procurement.mdm.exception.InErrorException
import com.procurement.mdm.model.dto.CommandMessage
import com.procurement.mdm.model.dto.ResponseDto
import com.procurement.mdm.model.dto.data.BD
import com.procurement.mdm.model.dto.data.ClassificationScheme
import com.procurement.mdm.model.dto.getResponseDto
import com.procurement.mdm.repository.CpvRepository
import com.procurement.mdm.repository.CurrencyRepository
import com.procurement.mdm.service.ValidationService
import org.springframework.stereotype.Service

interface BudgetDataService {

    fun createEi(cm: CommandMessage): ResponseDto

    fun createFs(cm: CommandMessage): ResponseDto
}

@Service
class BudgetDataServiceImpl(private val validationService: ValidationService,
                            private val organizationDataService: OrganizationDataService,
                            private val cpvRepository: CpvRepository,
                            private val currencyRepository: CurrencyRepository
) : BudgetDataService {

    override fun createEi(cm: CommandMessage): ResponseDto {
        val lang = cm.context.language
        val country = validationService.getCountry(languageCode = lang, countryCode = cm.context.country)
        val dto = getData(cm)
        val cpvCode = dto.tender?.classification?.id ?: throw InErrorException(ErrorType.INVALID_CPV)
        val cpvEntity = cpvRepository.findByCpvKeyCodeAndCpvKeyLanguageCode(
                code = cpvCode,
                languageCode = cm.context.language)
                ?: throw InErrorException(ErrorType.CPV_CODE_UNKNOWN)
        dto.tender?.apply {
            classification?.scheme = ClassificationScheme.CPV.value()
            classification?.description = cpvEntity.name
            mainProcurementCategory = cpvEntity.mainProcurementCategory
        }
        val buyer = dto.buyer ?: throw InErrorException(ErrorType.INVALID_BUYER)
        organizationDataService.processOrganization(buyer, country)

        return getResponseDto(data = dto, id = cm.id)
    }

    override fun createFs(cm: CommandMessage): ResponseDto {
        val country = validationService.getCountry(languageCode = cm.context.language, countryCode = cm.context.country)
        val entities = currencyRepository.findByCurrencyKeyLanguageCodeAndCountries(cm.context.language, country)
        val dto = getData(cm)
        val currencyCode = dto.planning?.budget?.amount?.currency ?: throw InErrorException(ErrorType.CURRENCY_UNKNOWN)
        entities.asSequence().firstOrNull { it.currencyKey?.code.equals(currencyCode) }
                ?: throw InErrorException(ErrorType.CURRENCY_UNKNOWN)

        val buyer = dto.buyer
        if (buyer != null) {
            organizationDataService.processOrganization(buyer, country)
        }

        val procuringEntity = dto.tender?.procuringEntity
        if (procuringEntity != null) {
            organizationDataService.processOrganization(procuringEntity, country)
        }

        return getResponseDto(data = dto, id = cm.id)
    }

    private fun getData(cm: CommandMessage): BD {
        cm.data ?: throw InErrorException(ErrorType.INVALID_DATA, cm.id)
        return toObject(BD::class.java, cm.data)
    }

}

