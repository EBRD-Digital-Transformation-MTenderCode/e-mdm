package com.procurement.mdm.service.preparation

import com.procurement.access.model.dto.items.*
import com.procurement.access.utils.toObject
import com.procurement.mdm.exception.ErrorType
import com.procurement.mdm.exception.InErrorException
import com.procurement.mdm.model.dto.CommandMessage
import com.procurement.mdm.model.dto.ResponseDto
import com.procurement.mdm.model.dto.getResponseDto
import com.procurement.mdm.model.entity.*
import com.procurement.mdm.repository.*
import com.procurement.mdm.service.ValidationService
import org.springframework.stereotype.Service

interface TenderInfoService {

    fun tenderInfo(cm: CommandMessage): ResponseDto
}

@Service
class TenderInfoServiceImpl(private val validationService: ValidationService,
                            private val cpvRepository: CpvRepository,
                            private val cpvsRepository: CpvsRepository,
                            private val unitRepository: UnitRepository,
                            private val translateRepository: TranslateRepository,
                            private val pmdRepository: PmdRepository) : TenderInfoService {

    override fun tenderInfo(cm: CommandMessage): ResponseDto {
        val lang = cm.context.language
        val pmd = cm.context.pmd?: throw InErrorException(ErrorType.INVALID_PMD)
        val language = validationService.getLanguage(languageCode = lang, internal = true)
        val country = validationService.getCountry(languageCode = lang, countryCode = cm.context.country)
        val dto = getData(cm)
        //common Class
        checkItemCodes(dto.tender.items, 3)
        val commonChars = getCommonChars(dto.tender.items, 3, 7)
        val commonClass = addCheckSum(commonChars)
        //items cpv
        val cpvCodes = getCpvCodes(dto.tender.items)
        val cpvKeys = cpvCodes.asSequence().map { CpvKey(it, language) }.toList()
        val cpvEntities = cpvRepository.findAllById(cpvKeys)
        if (cpvEntities.isEmpty()) throw InErrorException(ErrorType.INVALID_CPV)
        cpvEntities.asSequence().forEach { entity ->
            dto.tender.items.asSequence()
                    .filter { it.classification.id == entity.cpvKey?.code }
                    .forEach { setCpvData(it.classification, entity) }
        }
        //items cpvs
        val cpvsCodes = getCpvsCodes(dto.tender.items)
        val cpvsKeys = cpvsCodes.asSequence().map { CpvsKey(it, language) }.toList()
        val cpvsEntities = cpvsRepository.findAllById(cpvsKeys)
        if (cpvsEntities.isEmpty()) throw InErrorException(ErrorType.INVALID_CPVS)
        cpvsEntities.asSequence().forEach { entity ->
            dto.tender.items.asSequence().forEach { item ->
                item.additionalClassifications.asSequence()
                        .filter { it.id == entity.cpvsKey?.code }
                        .forEach { setCpvsData(it, entity) }
            }
        }
        //items unit
        val unitCodes = getUnitCodes(dto.tender.items)
        val unitKeys = unitCodes.asSequence().map { UnitKey(it, language) }.toList()
        val unitEntities = unitRepository.findAllById(unitKeys)
        if (unitEntities.isEmpty()) throw InErrorException(ErrorType.INVALID_UNIT)
        unitEntities.asSequence().forEach { entity ->
            dto.tender.items.asSequence()
                    .filter { it.unit.id == entity.unitKey?.code }
                    .forEach { seUnitData(it.unit, entity) }
        }
        //tender.classification
        val cpvEntity = cpvRepository.findByCpvKeyCodeAndCpvKeyLanguageCode(code = commonClass, languageCode = lang)
                ?: throw InErrorException(ErrorType.INVALID_COMMON_CPV, commonClass)
        //translate
        val smrCode = "submissionMethodRationale"
        val smrEntity = translateRepository.findByTranslateKeyCodeAndTranslateKeyLanguageCode(
                code = smrCode, languageCode = lang)
                ?: throw InErrorException(ErrorType.TRANSLATION_UNKNOWN, smrCode)
        val smdCode = "submissionMethodRationale"
        val smdEntity = translateRepository.findByTranslateKeyCodeAndTranslateKeyLanguageCode(
                code = smdCode, languageCode = lang)
                ?: throw InErrorException(ErrorType.TRANSLATION_UNKNOWN, smdCode)
        val pmdEntity = pmdRepository.findByPmdKeyCodeAndPmdKeyCountry(code = pmd, country = country)
                ?: throw InErrorException(ErrorType.TRANSLATION_UNKNOWN, pmd)
        //tender
        dto.tender.apply {
            classification = Classification(
                    id = commonClass,
                    description = cpvEntity.name,
                    scheme = Scheme.CPV.value())
            mainProcurementCategory = cpvEntity.mainProcurementCategory
            submissionMethodRationale = listOf(smrEntity.name)
            submissionMethodDetails = smdEntity.name
            procurementMethodDetails = pmdEntity.name
        }
        return getResponseDto(data = dto, id = cm.id)
    }

    private fun checkItemCodes(items: HashSet<Item>, charCount: Int) {
        if (items.asSequence().map { it.classification.id.take(charCount) }.toSet().size > 1) throw InErrorException(ErrorType.INVALID_ITEMS)
    }

    private fun getCommonChars(items: HashSet<Item>, countFrom: Int, countTo: Int): String {
        var commonChars = ""
        for (count in countFrom..countTo) {
            val itemClass = items.asSequence().map { it.classification.id.take(count) }.toSet()
            if (itemClass.size > 1) {
                return commonChars
            } else {
                commonChars = itemClass.first()
            }
        }
        return commonChars
    }

    private fun addCheckSum(commonChars: String): String {
        val classOfItems = commonChars.padEnd(8, '0')//09134230-8?(2)
        val n1 = classOfItems[0].toString().toInt()
        val n2 = classOfItems[1].toString().toInt()
        val n3 = classOfItems[2].toString().toInt()
        val n4 = classOfItems[3].toString().toInt()
        val n5 = classOfItems[4].toString().toInt()
        val n6 = classOfItems[5].toString().toInt()
        val n7 = classOfItems[6].toString().toInt()
        val n8 = classOfItems[7].toString().toInt()
        val checkSum: Int = (n1 * 3 + n2 * 7 + n3 * 1 + n4 * 3 + n5 * 7 + n6 * 1 + n7 * 3 + n8 * 7) % 10
        return "$classOfItems-$checkSum"
    }

    private fun getCpvCodes(items: HashSet<Item>): List<String> {
        return items.asSequence().map { it.classification.id }.toList()
    }

    private fun getUnitCodes(items: HashSet<Item>): List<String> {
        return items.asSequence().map { it.unit.id }.toList()
    }


    private fun getCpvsCodes(items: HashSet<Item>): List<String> {
        return items.asSequence().flatMap { it.additionalClassifications.asSequence() }.map { it.id }.toList()
    }

    private fun setCpvData(classification: Classification, entity: Cpv) {
        classification.scheme = Scheme.CPV.value()
        classification.description = entity.name
    }

    private fun setCpvsData(classification: Classification, entity: Cpvs) {
        classification.scheme = Scheme.CPVS.value()
        classification.description = entity.name
    }

    private fun seUnitData(unit: ItemUnit, entity: Units) {
        unit.name = entity.name
    }

    private fun getData(cm: CommandMessage): TenderInfo {
        cm.data ?: throw InErrorException(ErrorType.INVALID_DATA, cm.id)
        return toObject(TenderInfo::class.java, cm.data)
    }

}