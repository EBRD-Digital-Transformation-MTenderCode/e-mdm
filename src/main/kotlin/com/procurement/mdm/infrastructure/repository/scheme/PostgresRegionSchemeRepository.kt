package com.procurement.mdm.infrastructure.repository.scheme

import com.procurement.mdm.domain.entity.RegionEntity
import com.procurement.mdm.domain.model.code.CountryCode
import com.procurement.mdm.domain.model.code.LanguageCode
import com.procurement.mdm.domain.model.code.RegionCode
import com.procurement.mdm.domain.model.scheme.RegionScheme
import com.procurement.mdm.domain.repository.scheme.RegionSchemeRepository
import com.procurement.mdm.infrastructure.repository.AbstractRepository
import org.intellij.lang.annotations.Language
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import java.sql.ResultSet

@Repository
class PostgresRegionSchemeRepository(
    jdbcTemplate: NamedParameterJdbcTemplate
) : AbstractRepository(jdbcTemplate), RegionSchemeRepository {

    companion object {

        @Language("PostgreSQL")
        private const val SCHEME_EXISTS_SQL = """
            SELECT EXISTS(
                SELECT ls.code
                  FROM public.list_schemes AS ls
            INNER JOIN public.region_schemes AS rs
                    ON ls.id = rs.list_schemes_id                    
                 WHERE ls.code = :scheme
             )
            """

        @Language("PostgreSQL")
        private const val REGION_EXISTS_BY_SCHEME_SQL = """
            SELECT EXISTS(
                SELECT rs.code
                  FROM public.list_schemes AS ls
            INNER JOIN public.region_schemes AS rs
                    ON ls.id = rs.list_schemes_id
                 WHERE ls.code = :scheme
                   AND rs.code = :region
           )
        """

        @Language("PostgreSQL")
        private const val REGION_EXISTS_BY_SCHEME_AND_COUNTRY_SQL = """
            SELECT EXISTS(
                SELECT rs.code
                  FROM public.list_schemes AS ls
            INNER JOIN public.region_schemes AS rs
                    ON ls.id = rs.list_schemes_id
                 WHERE ls.code = :scheme
                   AND rs.country_code = :country
                   AND rs.code = :region                   
           )
        """

        @Language("PostgreSQL")
        private const val REGION_EXISTS_BY_COUNTRY_SQL = """
            SELECT EXISTS(
                SELECT rs.code
                  FROM public.region_schemes AS rs
                 WHERE rs.country_code = :country
                   AND rs.code = :region                   
           )
        """

        @Language("PostgreSQL")
        private const val FIND_BY_SQL = """
            SELECT ls.code AS scheme,
                   rs.code AS id,
                   rsi18n.description,
                   ls.uri
              FROM public.list_schemes AS ls
        INNER JOIN public.region_schemes AS rs
                ON ls.id = rs.list_schemes_id
        INNER JOIN public.region_schemes_i18n AS rsi18n
                ON rs.id = rsi18n.region_scheme_id
             WHERE ls.code = :scheme
               AND rs.code = :region
               AND rs.country_code = :country
               AND rsi18n.language_code = :language
            """
    }

    override fun existsBy(scheme: RegionScheme): Boolean = jdbcTemplate.queryForObject(
        SCHEME_EXISTS_SQL,
        mapOf(
            "scheme" to scheme.value.toUpperCase()
        ),
        Boolean::class.java
    )!!

    override fun existsBy(region: RegionCode, scheme: RegionScheme): Boolean = jdbcTemplate.queryForObject(
        REGION_EXISTS_BY_SCHEME_SQL,
        mapOf(
            "scheme" to scheme.value.toUpperCase(),
            "region" to region.value.toUpperCase()
        ),
        Boolean::class.java
    )!!

    override fun existsBy(region: RegionCode, scheme: RegionScheme, country: CountryCode): Boolean =
        jdbcTemplate.queryForObject(
            REGION_EXISTS_BY_SCHEME_AND_COUNTRY_SQL,
            mapOf(
                "scheme" to scheme.value.toUpperCase(),
                "country" to country.value.toUpperCase(),
                "region" to region.value.toUpperCase()
            ),
            Boolean::class.java
        )!!

    override fun existsBy(region: RegionCode, country: CountryCode): Boolean =
        jdbcTemplate.queryForObject(
            REGION_EXISTS_BY_COUNTRY_SQL,
            mapOf(
                "country" to country.value.toUpperCase(),
                "region" to region.value.toUpperCase()
            ),
            Boolean::class.java
        )!!

    override fun findBy(
        region: RegionCode, scheme: RegionScheme, country: CountryCode, language: LanguageCode
    ): RegionEntity? =
        getObject(
            sql = FIND_BY_SQL,
            params = mapOf(
                "region" to region.value.toUpperCase(),
                "country" to country.value.toUpperCase(),
                "language" to language.value.toUpperCase(),
                "scheme" to scheme.value.toUpperCase()
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