package com.procurement.mdm.infrastructure.repository.language

import com.procurement.mdm.domain.model.code.LanguageCode
import com.procurement.mdm.domain.repository.AdvancedLanguageRepository
import com.procurement.mdm.infrastructure.repository.AbstractRepositoryTest
import com.procurement.mdm.infrastructure.repository.loadSql
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class AdvancedLanguageRepositoryIT : AbstractRepositoryTest() {

    companion object {
        private val LANGUAGE_CODE = LanguageCode("ro")
        private val UNKNOWN_LANGUAGE_CODE = LanguageCode("ul")
    }

    @Autowired
    private lateinit var repository: AdvancedLanguageRepository

    @Test
    fun `Language is exists`() {
        val sqlLanguages = loadSql("sql/languages_init_data.sql")
        executeSQLScript(sqlLanguages)
        assertTrue(repository.exists(code = LANGUAGE_CODE))
    }

    @Test
    fun `Language is not exists`() {
        val sqlLanguages = loadSql("sql/languages_init_data.sql")
        executeSQLScript(sqlLanguages)
        assertFalse(repository.exists(code = UNKNOWN_LANGUAGE_CODE))
    }

    @Test
    fun `Language is not exists (database is empty)`() {
        assertFalse(repository.exists(code = LANGUAGE_CODE))
    }
}
