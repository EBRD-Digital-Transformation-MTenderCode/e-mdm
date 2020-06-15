package com.procurement.mdm.infrastructure.repository.criteria

import com.procurement.mdm.domain.entity.CriteriaEntity
import com.procurement.mdm.domain.model.Phase
import com.procurement.mdm.domain.model.Pmd
import com.procurement.mdm.domain.model.code.CountryCode
import com.procurement.mdm.domain.model.code.LanguageCode
import com.procurement.mdm.domain.repository.criteria.CriteriaRepository
import com.procurement.mdm.infrastructure.repository.AbstractRepository
import org.intellij.lang.annotations.Language
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import java.sql.ResultSet

@Repository
class PostgresCriteriaRepository(
    jdbcTemplate: NamedParameterJdbcTemplate
) : AbstractRepository(jdbcTemplate), CriteriaRepository {

    companion object {
        @Language("PostgreSQL")
        private const val FIND_BY_SQL = """
            SELECT cr.code AS id,
                   cri18n.title,
                   cri18n.description          
              FROM public.criterion AS cr    
        INNER JOIN public.criterion_i18n cri18n 
                ON cr.id = cri18n.criteria_id
             WHERE cr.country_code = :country
               AND cr.pmd = :pmd
               AND cr.phase = :phase
               AND cri18n.language_code = :language               
            """
    }

    override fun findBy(
        country: CountryCode, pmd: Pmd, language: LanguageCode, phase: Phase
    ): List<CriteriaEntity> = getListObjects(
        sql = FIND_BY_SQL,
        params = mapOf(
            "country" to country.value.toUpperCase(),
            "pmd" to pmd.value.toUpperCase(),
            "language" to language.value.toUpperCase(),
            "phase" to phase.value.toUpperCase()
        ),
        mapper = criteriaRowMapper
    )

    private val criteriaRowMapper: (ResultSet, Int) -> CriteriaEntity = { rs, _ ->
        CriteriaEntity(
            id = rs.getString("id"),
            title = rs.getString("title"),
            description = rs.getString("description")
        )
    }
}