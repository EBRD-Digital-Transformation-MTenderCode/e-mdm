package com.procurement.mdm.infrastructure.repository.requirement.group

import com.procurement.mdm.domain.entity.RequirementGroupEntity
import com.procurement.mdm.domain.model.Phase
import com.procurement.mdm.domain.model.Pmd
import com.procurement.mdm.domain.model.code.CountryCode
import com.procurement.mdm.domain.model.code.CriterionCode
import com.procurement.mdm.domain.model.code.LanguageCode
import com.procurement.mdm.domain.repository.requirement.group.RequirementGroupRepository
import com.procurement.mdm.infrastructure.repository.AbstractRepository
import org.intellij.lang.annotations.Language
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import java.sql.ResultSet

@Repository
class PostgresRequirementGroupRepository(
    jdbcTemplate: NamedParameterJdbcTemplate
) : AbstractRepository(jdbcTemplate), RequirementGroupRepository {

    companion object {
        @Language("PostgreSQL")
        private const val FIND_BY_SQL = """
            SELECT rg.code AS id,                   
                   rgi18n.description          
              FROM public.requirement_groups AS rg    
        LEFT JOIN public.requirement_groups_i18n rgi18n 
                ON rg.id = rgi18n.requirement_group_id
             WHERE rg.country_code = :country
               AND rg.pmd = :pmd
               AND rg.phase = :phase
               AND rg.criterion_code = :criterion
               AND (rgi18n.language_code = :language
                OR rgi18n.language_code IS NULL);           
            """
    }

    override fun findBy(
        country: CountryCode, pmd: Pmd, language: LanguageCode, phase: Phase, criterion: CriterionCode
    ): List<RequirementGroupEntity> = getListObjects(
        sql = FIND_BY_SQL,
        params = mapOf(
            "country" to country.value.toUpperCase(),
            "pmd" to pmd.value.toUpperCase(),
            "language" to language.value.toUpperCase(),
            "phase" to phase.value.toUpperCase(),
            "criterion" to criterion.value.toUpperCase()
        ),
        mapper = requirementGroupRowMapper
    )

    private val requirementGroupRowMapper: (ResultSet, Int) -> RequirementGroupEntity = { rs, _ ->
        RequirementGroupEntity(
            id = rs.getString("id"),
            description = rs.getString("description")
        )
    }
}