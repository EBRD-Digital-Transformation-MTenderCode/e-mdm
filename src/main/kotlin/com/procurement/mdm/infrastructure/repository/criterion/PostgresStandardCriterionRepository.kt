package com.procurement.mdm.infrastructure.repository.criterion

import com.procurement.mdm.domain.model.ProcurementCategory
import com.procurement.mdm.domain.model.code.CountryCode
import com.procurement.mdm.domain.model.code.LanguageCode
import com.procurement.mdm.domain.model.criterion.CriterionCategory
import com.procurement.mdm.domain.repository.criterion.StandardCriterionRepository
import com.procurement.mdm.infrastructure.repository.AbstractRepository
import com.procurement.mdm.model.record.StandardCriterionRecord
import org.intellij.lang.annotations.Language
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import java.sql.ResultSet

@Repository
class PostgresStandardCriterionRepository(
    jdbcTemplate: NamedParameterJdbcTemplate
) : AbstractRepository(jdbcTemplate), StandardCriterionRepository {

    companion object {

        const val PARAM_COUNTRY = "country"
        const val PARAM_LANGUAGE = "language"
        const val PARAM_PROCUREMENT_CATEGORY = "procurementCategory"
        const val PARAM_CRITERIA_CATEGORY = "criteriaCategory"


        @Language("PostgreSQL")
        private const val FIND_ALL_BY_LOCALIZATION_SQL = """
            SELECT criterion.id,
                   criterion.country_code,
                   criterion.language_code,
                   criterion.data
              FROM public.standard_criteria AS criterion
             WHERE criterion.country_code = :country
               AND criterion.language_code = :language
            """

        @Language("PostgreSQL")
        private const val FIND_ALL_BY_PROCUREMENT_CATEGORY_SQL = """
            SELECT criterion.id,
                   criterion.country_code,
                   criterion.language_code,
                   criterion.data
              FROM public.standard_criteria AS criterion
             WHERE criterion.country_code = :country
               AND criterion.language_code = :language
               AND criterion.data -> 'mainProcurementCategories' @> ('[ "' || :procurementCategory || '" ]')::jsonb
               OR (criterion.data -> 'mainProcurementCategories') IS NULL
            """

        @Language("PostgreSQL")
        private const val FIND_ALL_BY_CRITERION_CATEGORY_SQL = """
            SELECT criterion.id,
                   criterion.country_code,
                   criterion.language_code,
                   criterion.data
              FROM public.standard_criteria AS criterion
             WHERE criterion.country_code = :country
               AND criterion.language_code = :language
               AND criterion.data 
                    -> 'classification'
                    ->> 'id' like  :criteriaCategory || '%'
            """


        @Language("PostgreSQL")
        private const val FIND_ALL_BY_CATEGORIES_SQL = """
            SELECT criterion.id,
                   criterion.country_code,
                   criterion.language_code,
                   criterion.data
              FROM public.standard_criteria AS criterion
             WHERE criterion.country_code = :country
               AND criterion.language_code = :language
               AND criterion.data 
                  -> 'classification'
                  ->> 'id' like  :criteriaCategory || '%'
               AND criterion.data -> 'mainProcurementCategories' @> ('[ "' || :procurementCategory || '" ]')::jsonb
                OR (criterion.data -> 'mainProcurementCategories') IS NULL
             
            """
    }


    override fun findBy(country: CountryCode, language: LanguageCode): List<StandardCriterionRecord> {
        return getListObjects(
            sql = FIND_ALL_BY_LOCALIZATION_SQL,
            params = mapOf(
                PARAM_COUNTRY to country.value.toUpperCase(),
                PARAM_LANGUAGE to language.value.toUpperCase()
            ),
            mapper = criterionRowMapper
        )

    }

    override fun findBy(country: CountryCode, language: LanguageCode, mainProcurementCategory: ProcurementCategory): List<StandardCriterionRecord> {
        return getListObjects(
            sql = FIND_ALL_BY_PROCUREMENT_CATEGORY_SQL,
            params = mapOf(
                PARAM_COUNTRY to country.value.toUpperCase(),
                PARAM_LANGUAGE to language.value.toUpperCase(),
                PARAM_PROCUREMENT_CATEGORY to mainProcurementCategory.key
            ),
            mapper = criterionRowMapper
        )
    }

    override fun findBy(country: CountryCode, language: LanguageCode, criteriaCategory: CriterionCategory): List<StandardCriterionRecord> {
        return getListObjects(
            sql = FIND_ALL_BY_CRITERION_CATEGORY_SQL,
            params = mapOf(
                PARAM_COUNTRY to country.value.toUpperCase(),
                PARAM_LANGUAGE to language.value.toUpperCase(),
                PARAM_CRITERIA_CATEGORY to criteriaCategory.key
            ),
            mapper = criterionRowMapper
        )
    }

    override fun findBy(country: CountryCode, language: LanguageCode, mainProcurementCategory: ProcurementCategory, criteriaCategory: CriterionCategory): List<StandardCriterionRecord> {
        return getListObjects(
            sql = FIND_ALL_BY_CATEGORIES_SQL,
            params = mapOf(
                PARAM_COUNTRY to country.value.toUpperCase(),
                PARAM_LANGUAGE to language.value.toUpperCase(),
                PARAM_PROCUREMENT_CATEGORY to mainProcurementCategory.key,
                PARAM_CRITERIA_CATEGORY to criteriaCategory.key
            ),
            mapper = criterionRowMapper
        )
    }

    private val criterionRowMapper: (ResultSet, Int) -> StandardCriterionRecord = { rs, _ ->
        StandardCriterionRecord(
            id = rs.getString("id"),
            country = rs.getString("country_code"),
            language = rs.getString("language_code"),
            data = rs.getString("data")
        )
    }
}