package com.procurement.mdm.infrastructure.repository.requirement

import com.procurement.mdm.domain.entity.RequirementEntity
import com.procurement.mdm.domain.model.Phase
import com.procurement.mdm.domain.model.Pmd
import com.procurement.mdm.domain.model.code.CountryCode
import com.procurement.mdm.domain.model.code.LanguageCode
import com.procurement.mdm.domain.model.code.RequirementGroupCode
import com.procurement.mdm.domain.repository.requirement.RequirementRepository
import com.procurement.mdm.infrastructure.repository.AbstractRepository
import org.intellij.lang.annotations.Language
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import java.sql.ResultSet

@Repository
class PostgresRequirementRepository(
    jdbcTemplate: NamedParameterJdbcTemplate
) : AbstractRepository(jdbcTemplate), RequirementRepository {

    companion object {
        @Language("PostgreSQL")
        private const val FIND_BY_SQL = """
            SELECT r.code AS id,
                   ri18n.title,            
                   ri18n.description          
              FROM public.requirements AS r    
        INNER JOIN public.requirements_i18n ri18n 
                ON r.id = ri18n.requirement_id
             WHERE r.country_code = :country
               AND r.pmd = :pmd
               AND r.phase = :phase
               AND r.requirement_group_code = :requirement_group
               AND ri18n.language_code = :language;                         
            """
    }

    override fun findBy(
        country: CountryCode, pmd: Pmd, language: LanguageCode, phase: Phase, requirementGroup: RequirementGroupCode
    ): List<RequirementEntity> = getListObjects(
        sql = FIND_BY_SQL,
        params = mapOf(
            "country" to country.value.toUpperCase(),
            "pmd" to pmd.value.toUpperCase(),
            "language" to language.value.toUpperCase(),
            "phase" to phase.value.toUpperCase(),
            "requirement_group" to requirementGroup.value.toUpperCase()
        ),
        mapper = requirementRowMapper
    )

    private val requirementRowMapper: (ResultSet, Int) -> RequirementEntity = { rs, _ ->
        RequirementEntity(
            id = rs.getString("id"),
            title = rs.getString("title"),
            description = rs.getString("description")
        )
    }
}