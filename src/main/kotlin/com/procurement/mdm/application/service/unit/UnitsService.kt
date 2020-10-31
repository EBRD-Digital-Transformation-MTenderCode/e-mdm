package com.procurement.mdm.application.service.unit

import com.procurement.mdm.application.exception.UnitLanguageNotFoundException
import com.procurement.mdm.application.exception.UnitNotFoundException
import com.procurement.mdm.application.exception.UnitTranslationNotFoundException
import com.procurement.mdm.application.service.unit.UnitsServiceImpl.UnitById.unitByLangFilter
import com.procurement.mdm.domain.model.code.LanguageCode
import com.procurement.mdm.domain.model.unit.Unit
import com.procurement.mdm.domain.repository.AdvancedLanguageRepository
import com.procurement.mdm.model.entity.Units
import com.procurement.mdm.repository.UnitRepository
import org.springframework.stereotype.Service

interface UnitsService {
    fun getById(id: String, language: String): Unit
}

@Service
class UnitsServiceImpl(
    private val advancedLanguageRepository: AdvancedLanguageRepository,
    private val unitRepository: UnitRepository
) : UnitsService {

    override fun getById(id: String, language: String): Unit {
        val languageCode = LanguageCode(language)
            .apply { validation(this) }

        val storedUnits = unitRepository.findByUnitKeyCode(code = id)
        return if (storedUnits.isEmpty())
            throw UnitNotFoundException(id = id)
        else
            storedUnits
                .find { unit -> unitByLangFilter(unit, languageCode) }
                ?.let { unit -> Unit(id = unit.unitKey!!.code!!, name = unit.name) }
                ?: throw UnitTranslationNotFoundException(language = language)
    }

    private fun validation(languageCode: LanguageCode) {
        if (advancedLanguageRepository.exists(languageCode).not())
            throw UnitLanguageNotFoundException(language = languageCode)
    }

    private object UnitById {

        fun unitByLangFilter(storedUnit: Units, targetLang: LanguageCode): Boolean {
            return storedUnit.unitKey!!.language!!.code.toUpperCase() == targetLang.value.toUpperCase()
        }
    }
}

