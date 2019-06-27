package com.procurement.mdm.domain.repository

import com.procurement.mdm.domain.model.code.LanguageCode

interface AdvancedLanguageRepository {
    fun exists(code: LanguageCode): Boolean
}
