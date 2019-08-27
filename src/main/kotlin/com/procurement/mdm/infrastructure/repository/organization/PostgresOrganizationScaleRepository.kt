package com.procurement.mdm.infrastructure.repository.organization

import com.procurement.mdm.domain.model.code.CountryCode
import com.procurement.mdm.domain.repository.organization.OrganizationScaleRepository
import com.procurement.mdm.infrastructure.repository.AbstractRepository
import org.intellij.lang.annotations.Language
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import java.sql.ResultSet

@Repository
class PostgresOrganizationScaleRepository(
    jdbcTemplate: NamedParameterJdbcTemplate
) : AbstractRepository(jdbcTemplate),
    OrganizationScaleRepository {

    companion object {

        @Language("PostgreSQL")
        private const val FIND_ALL_ONLY_CODE_SQL = """
            SELECT os.code
              FROM public.organization_scale_used AS osu
        INNER JOIN public.organization_scales os
                ON osu.scale_id = os.id
             WHERE osu.country_code = :country_code
            """
    }

    override fun findAllOnlyCode(country: CountryCode): List<String> = getListObjects(
        sql = FIND_ALL_ONLY_CODE_SQL,
        params = mapOf(
            "country_code" to country.value.toUpperCase()
        ),
        mapper = scaleCodeRowMapper
    )

    private val scaleCodeRowMapper: (ResultSet, Int) -> String = { rs, _ -> rs.getString("code") }
}
