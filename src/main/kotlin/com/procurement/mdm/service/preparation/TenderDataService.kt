package com.procurement.mdm.service.preparation

import com.procurement.access.utils.toObject
import com.procurement.mdm.exception.ErrorType
import com.procurement.mdm.exception.InErrorException
import com.procurement.mdm.model.dto.CommandMessage
import com.procurement.mdm.model.dto.ResponseDto
import com.procurement.mdm.model.dto.data.*
import com.procurement.mdm.model.dto.getResponseDto
import com.procurement.mdm.model.entity.*
import com.procurement.mdm.repository.*
import com.procurement.mdm.service.ValidationService
import org.springframework.stereotype.Service

interface TenderDataService {

    fun createTender(cm: CommandMessage): ResponseDto
}

@Service
class TenderDataServiceServiceImpl(private val validationService: ValidationService,
                                   private val cpvRepository: CpvRepository,
                                   private val cpvsRepository: CpvsRepository,
                                   private val unitRepository: UnitRepository,
                                   private val translateRepository: TranslateRepository,
                                   private val pmdRepository: PmdRepository) : TenderDataService {

    override fun createTender(cm: CommandMessage): ResponseDto {
        val lang = cm.context.language
        val pmd = cm.context.pmd ?: throw InErrorException(ErrorType.INVALID_PMD)
        val language = validationService.getLanguage(languageCode = lang, internal = true)
        val country = validationService.getCountry(languageCode = lang, countryCode = cm.context.country)
        val dto = getData(cm)
        if (dto.tender.items != null) {
            processItems(dto, language)
        }
        processTranslate(dto, country, lang, pmd)
        return getResponseDto(data = dto, id = cm.id)
    }

    private fun processTranslate(dto: TD, country: Country, lang: String, pmd: String) {
        dto.tender.apply {
            submissionMethodRationale = listOf(getTranslate("submissionMethodRationale", lang))
            submissionMethodDetails = getTranslate("submissionMethodDetails", lang)
            procurementMethodDetails = getPmd(pmd, country)
            eligibilityCriteria = getTranslate("eligibilityCriteria", lang)
        }
    }

    private fun processItems(dto: TD, language: Language) {
        val items = dto.tender.items ?: return
        //common Class
        checkItemCodes(items, 3)
        //data cpv
        val cpvCodes = getCpvCodes(items)
        if (cpvCodes.isNotEmpty()) {
            val cpvKeys = cpvCodes.asSequence().map { CpvKey(it, language) }.toHashSet()
            val cpvEntities = cpvRepository.findAllById(cpvKeys)
            if (cpvEntities.isEmpty() || cpvEntities.size != cpvKeys.size) throw InErrorException(ErrorType.INVALID_CPV)
            cpvEntities.asSequence().forEach { entity ->
                items.asSequence()
                        .filter { it.classification.id == entity.cpvKey?.code }
                        .forEach { setCpvData(it.classification, entity) }
            }
        }
        //data cpvs
        val cpvsCodes = getCpvsCodes(items)
        if (cpvsCodes.isNotEmpty()) {
            val cpvsKeys = cpvsCodes.asSequence().map { CpvsKey(it, language) }.toHashSet()
            val cpvsEntities = cpvsRepository.findAllById(cpvsKeys)
            if (cpvsEntities.isEmpty() || cpvsEntities.size != cpvsKeys.size) throw InErrorException(ErrorType.INVALID_CPVS)
            cpvsEntities.asSequence().forEach { entity ->
                items.asSequence().forEach { item ->
                    item.additionalClassifications?.asSequence()
                            ?.filter { it.id == entity.cpvsKey?.code }
                            ?.forEach { setCpvsData(it, entity) }
                }
            }
        }
        //data unit
        val unitCodes = getUnitCodes(items)
        if (unitCodes.isNotEmpty()) {
            val unitKeys = unitCodes.asSequence().map { UnitKey(it, language) }.toHashSet()
            val unitEntities = unitRepository.findAllById(unitKeys)
            if (unitEntities.isEmpty() || unitEntities.size != unitKeys.size) throw InErrorException(ErrorType.INVALID_UNIT)
            unitEntities.asSequence().forEach { entity ->
                items.asSequence()
                        .filter { it.unit.id == entity.unitKey?.code }
                        .forEach { seUnitData(it.unit, entity) }
            }
        }
        //tender.classification
        val commonChars = getCommonChars(items, 3, 7)
        val commonClass = getCommonClass(commonChars)
        val cpvEntity = cpvRepository.getCommonClass(code = commonClass, language = language)
                ?: throw InErrorException(ErrorType.INVALID_COMMON_CPV, commonClass)
        dto.tender.apply {
            classification = ClassificationTD(
                    id = cpvEntity.cpvKey?.code!!,
                    description = cpvEntity.name,
                    scheme = ClassificationScheme.CPV.value())
            mainProcurementCategory = cpvEntity.mainProcurementCategory
        }
    }

    private fun getTranslate(code: String, lang: String): String {
        val entity = translateRepository.findByTranslateKeyCodeAndTranslateKeyLanguageCode(
                code = code, languageCode = lang)
                ?: throw InErrorException(ErrorType.TRANSLATION_UNKNOWN, code)
        return entity.name
    }

    private fun getPmd(code: String, country: Country): String {
        val entity = pmdRepository.findByPmdKeyCodeAndPmdKeyCountry(code = code, country = country)
                ?: throw InErrorException(ErrorType.TRANSLATION_UNKNOWN, code)
        return entity.name
    }

    private fun checkItemCodes(items: HashSet<ItemTD>, charCount: Int) {
        if (items.asSequence().map { it.classification.id.take(charCount) }.toSet().size > 1)
            throw InErrorException(ErrorType.INVALID_ITEMS)
    }

    private fun getCommonChars(items: HashSet<ItemTD>, countFrom: Int, countTo: Int): String {
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

    private fun getCommonClass(commonChars: String): String {
        return commonChars.padEnd(8, '0')//09134230-8?(2)
//        val classOfItems = commonChars.padEnd(8, '0')//09134230-8?(2)
//        val n1 = classOfItems[0].toString().toInt()
//        val n2 = classOfItems[1].toString().toInt()
//        val n3 = classOfItems[2].toString().toInt()
//        val n4 = classOfItems[3].toString().toInt()
//        val n5 = classOfItems[4].toString().toInt()
//        val n6 = classOfItems[5].toString().toInt()
//        val n7 = classOfItems[6].toString().toInt()
//        val n8 = classOfItems[7].toString().toInt()
//        val checkSum: Int = (n1 * 3 + n2 * 7 + n3 * 1 + n4 * 3 + n5 * 7 + n6 * 1 + n7 * 3 + n8 * 7) % 10
//        return "$classOfItems-$checkSum"
    }

    private fun getCpvCodes(items: HashSet<ItemTD>): List<String> {
        return items.asSequence().map { it.classification.id }.toList()
    }

    private fun getUnitCodes(items: HashSet<ItemTD>): List<String> {
        return items.asSequence().map { it.unit.id }.toList()
    }


    private fun getCpvsCodes(items: HashSet<ItemTD>): List<String> {
        val cpvsCodes = arrayListOf<String>()
        items.forEach { item ->
            item.additionalClassifications?.let {
                item.additionalClassifications.forEach { ac ->
                    cpvsCodes.add(ac.id)
                }
            }
        }
        return cpvsCodes
    }

    private fun setCpvData(classification: ClassificationTD, entity: Cpv) {
        classification.scheme = ClassificationScheme.CPV.value()
        classification.description = entity.name
    }

    private fun setCpvsData(classification: ClassificationTD, entity: Cpvs) {
        classification.scheme = ClassificationScheme.CPVS.value()
        classification.description = entity.name
    }

    private fun seUnitData(unit: ItemUnitTD, entity: Units) {
        unit.name = entity.name
    }

    private fun getData(cm: CommandMessage): TD {
        cm.data ?: throw InErrorException(ErrorType.INVALID_DATA, cm.id)
        return toObject(TD::class.java, cm.data)
    }

}