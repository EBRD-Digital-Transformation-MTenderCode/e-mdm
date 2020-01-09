package com.procurement.mdm.infrastructure.repository.organization

import com.procurement.mdm.domain.model.code.CountryCode
import com.procurement.mdm.domain.repository.organization.OrganizationSchemeRepository
import com.procurement.mdm.infrastructure.repository.AbstractRepository
import org.intellij.lang.annotations.Language
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import java.sql.ResultSet

@Repository
class PostgresOrganizationSchemeRepository(
    jdbcTemplate: NamedParameterJdbcTemplate
) : AbstractRepository(jdbcTemplate), OrganizationSchemeRepository {

    companion object {

        @Language("PostgreSQL")
        private const val FIND_ALL_ONLY_CODE_SQL = """
            SELECT ls.code
              FROM public.organization_scheme_used AS osu
        INNER JOIN public.list_schemes AS ls
                ON osu.list_schemes_id = ls.id
             WHERE osu.country_code = :country_code
            """
    }

    override fun find(country: CountryCode): List<String> = getListObjects(
        sql = FIND_ALL_ONLY_CODE_SQL,
        params = mapOf(
            "country_code" to country.value.toUpperCase()
        ),
        mapper = schemeCodeRowMapper
    )

    private val schemeCodeRowMapper: (ResultSet, Int) -> String = { rs, _ -> rs.getString("code") }
}
