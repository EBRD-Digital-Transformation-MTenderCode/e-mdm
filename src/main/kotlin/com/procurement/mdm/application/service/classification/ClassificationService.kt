package com.procurement.mdm.application.service.classification

import com.procurement.mdm.application.exception.ClassificationLanguageNotFoundException
import com.procurement.mdm.application.exception.ClassificationNotFoundException
import com.procurement.mdm.application.exception.ClassificationTranslationNotFoundException
import com.procurement.mdm.application.exception.IncorrectClassificationSchemeException
import com.procurement.mdm.application.service.classification.ClassificationServiceImpl.ClassificationById.cpvByLangFilter
import com.procurement.mdm.domain.model.classification.Classification
import com.procurement.mdm.domain.model.code.LanguageCode
import com.procurement.mdm.domain.repository.AdvancedLanguageRepository
import com.procurement.mdm.model.dto.data.ClassificationScheme
import com.procurement.mdm.model.entity.Cpv
import com.procurement.mdm.repository.CpvRepository
import org.springframework.stereotype.Service

interface ClassificationService {
    fun getById(id: String, language: String, scheme: String): Classification
}

@Service
class ClassificationServiceImpl(
    private val cpvRepository: CpvRepository,
    private val advancedLanguageRepository: AdvancedLanguageRepository
) : ClassificationService {

    override fun getById(id: String, language: String, scheme: String): Classification {
        val languageCode = LanguageCode(language)
            .apply { validation(this) }

        val parsedScheme = parseScheme(scheme)
        return when (parsedScheme) {
            ClassificationScheme.CPV -> {
                val storedClassifications = cpvRepository.findByCpvKeyCode(code = id)
                if (storedClassifications.isEmpty())
                    throw ClassificationNotFoundException(id = id)
                else
                    storedClassifications
                        .find { cpv -> cpvByLangFilter(cpv, languageCode) }
                        ?.let { cpv -> Classification(id = cpv.cpvKey!!.code!!, description = cpv.name) }
                        ?: throw ClassificationTranslationNotFoundException(language = language)
            }
            ClassificationScheme.CPC,
            ClassificationScheme.CPVS,
            ClassificationScheme.GSIN,
            ClassificationScheme.OKDP,
            ClassificationScheme.OKPD,
            ClassificationScheme.UNSPSC,
            null -> throw IncorrectClassificationSchemeException(scheme)
        }
    }

    private fun validation(languageCode: LanguageCode) {
        if (advancedLanguageRepository.exists(languageCode).not())
            throw ClassificationLanguageNotFoundException(language = languageCode)
    }

    fun parseScheme(scheme: String): ClassificationScheme? =
        try {
            ClassificationScheme.fromValue(scheme)
        } catch (expected: Exception) {
            null
        }

    private object ClassificationById {

        fun cpvByLangFilter(storedCpv: Cpv, targetLang: LanguageCode): Boolean {
            return storedCpv.cpvKey!!.language!!.code.toUpperCase() == targetLang.value.toUpperCase()
        }
    }
}
