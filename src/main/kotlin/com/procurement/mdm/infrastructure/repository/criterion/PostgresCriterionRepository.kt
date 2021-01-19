package com.procurement.mdm.infrastructure.repository.criterion

import com.procurement.mdm.domain.entity.CriterionEntity
import com.procurement.mdm.domain.model.Phase
import com.procurement.mdm.domain.model.Pmd
import com.procurement.mdm.domain.model.code.CountryCode
import com.procurement.mdm.domain.model.code.LanguageCode
import com.procurement.mdm.domain.repository.criterion.CriterionRepository
import com.procurement.mdm.infrastructure.repository.AbstractRepository
import org.intellij.lang.annotations.Language
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import java.sql.ResultSet

@Repository
class PostgresCriterionRepository(
    jdbcTemplate: NamedParameterJdbcTemplate
) : AbstractRepository(jdbcTemplate), CriterionRepository {

    companion object {
        @Language("PostgreSQL")
        private const val FIND_BY_SQL = """
            SELECT cr.code AS id,
                   cri18n.title,
                   cri18n.description,
                   classifications.code as classification_id,
                   schemes.code as scheme         
              FROM public.criteria AS cr    
        INNER JOIN public.criteria_i18n cri18n 
                ON cr.id = cri18n.criterion_id
        INNER JOIN criteria_classifications classifications
                ON cr.classification_id = classifications.id
        INNER JOIN criteria_classification_schemes schemes
                ON classifications.scheme_id = schemes.id    
             WHERE cr.country_code = :country
               AND cr.pmd = :pmd
               AND cr.phase = :phase
               AND cri18n.language_code = :language               
            """
    }

    override fun findBy(
        country: CountryCode, pmd: Pmd, language: LanguageCode, phase: Phase
    ): List<CriterionEntity> = getListObjects(
        sql = FIND_BY_SQL,
        params = mapOf(
            "country" to country.value.toUpperCase(),
            "pmd" to pmd.value.toUpperCase(),
            "language" to language.value.toUpperCase(),
            "phase" to phase.value.toUpperCase()
        ),
        mapper = criterionRowMapper
    )

    private val criterionRowMapper: (ResultSet, Int) -> CriterionEntity = { rs, _ ->
        CriterionEntity(
            id = rs.getString("id"),
            title = rs.getString("title"),
            description = rs.getString("description"),
            classification = CriterionEntity.Classification(
                id = rs.getString("classification_id"),
                scheme = rs.getString("scheme")
            )
        )
    }
}