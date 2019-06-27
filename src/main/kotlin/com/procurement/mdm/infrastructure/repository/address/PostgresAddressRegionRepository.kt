package com.procurement.mdm.infrastructure.repository.address

import com.procurement.mdm.domain.entity.RegionEntity
import com.procurement.mdm.domain.model.code.CountryCode
import com.procurement.mdm.domain.model.code.LanguageCode
import com.procurement.mdm.domain.model.code.RegionCode
import com.procurement.mdm.domain.repository.address.AddressRegionRepository
import com.procurement.mdm.infrastructure.repository.AbstractRepository
import org.intellij.lang.annotations.Language
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import java.sql.ResultSet

@Repository
class PostgresAddressRegionRepository(
    jdbcTemplate: NamedParameterJdbcTemplate
) : AbstractRepository(jdbcTemplate), AddressRegionRepository {

    companion object {

        @Language("PostgreSQL")
        private const val FIND_BY_ID_SQL = """
            SELECT ls.code AS scheme,
                   ars.code AS id,
                   arsi18n.description,
                   ls.uri
              FROM public.list_schemes AS ls
        INNER JOIN public.address_regional_schemes AS ars
                ON ls.id = ars.list_schemes_id
        INNER JOIN public.address_regional_schemes_i18n AS arsi18n
                ON ars.id = arsi18n.address_regional_scheme_id
             WHERE ls.id = (SELECT list_schemes_id
                              FROM public.address_regional_scheme_used
                             WHERE country_code = :country_code)
               AND ars.code = :code
               AND ars.country_code = :country_code
               AND arsi18n.language_code = :language_code
            """
    }

    override fun findBy(region: RegionCode, country: CountryCode, language: LanguageCode): RegionEntity? =
        getObject(
            sql = FIND_BY_ID_SQL,
            params = mapOf(
                "code" to region.value.toUpperCase(),
                "country_code" to country.value.toUpperCase(),
                "language_code" to language.value.toUpperCase()
            ),
            mapper = regionRowMapper
        )

    private val regionRowMapper: (ResultSet, Int) -> RegionEntity = { rs, _ ->
        RegionEntity(
            scheme = rs.getString("scheme"),
            id = rs.getString("id"),
            description = rs.getString("description"),
            uri = rs.getString("uri")
        )
    }
}
