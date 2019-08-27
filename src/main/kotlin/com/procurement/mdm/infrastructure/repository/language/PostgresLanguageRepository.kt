package com.procurement.mdm.infrastructure.repository.language

import com.procurement.mdm.domain.model.code.LanguageCode
import com.procurement.mdm.domain.repository.AdvancedLanguageRepository
import com.procurement.mdm.infrastructure.repository.AbstractRepository
import org.intellij.lang.annotations.Language
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class PostgresLanguageRepository(
    jdbcTemplate: NamedParameterJdbcTemplate
) : AbstractRepository(jdbcTemplate), AdvancedLanguageRepository {

    companion object {
        @Language("PostgreSQL")
        private const val LANGUAGE_EXISTS_SQL = """
            SELECT EXISTS(
                SELECT code
                  FROM public.languages
                 WHERE code = :code)
        """
    }

    override fun exists(code: LanguageCode): Boolean =
        jdbcTemplate.queryForObject(
            LANGUAGE_EXISTS_SQL,
            mapOf(
                "code" to code.value.toUpperCase()
            ),
            Boolean::class.java
        )!!
}
