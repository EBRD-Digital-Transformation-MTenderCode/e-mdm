package com.procurement.mdm.infrastructure.repository.scheme

import com.procurement.mdm.domain.entity.LocalityEntity
import com.procurement.mdm.domain.model.code.LanguageCode
import com.procurement.mdm.domain.model.code.LocalityCode
import com.procurement.mdm.domain.model.code.RegionCode
import com.procurement.mdm.domain.model.scheme.LocalityScheme
import com.procurement.mdm.domain.repository.scheme.LocalitySchemeRepository
import com.procurement.mdm.infrastructure.repository.AbstractRepository
import org.intellij.lang.annotations.Language
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import java.sql.ResultSet

@Repository
class PostgresLocalitySchemeRepository(
    jdbcTemplate: NamedParameterJdbcTemplate
) : AbstractRepository(jdbcTemplate), LocalitySchemeRepository {

    companion object {

        @Language("PostgreSQL")
        private const val SCHEME_EXISTS_SQL = """
            SELECT EXISTS(
                SELECT ls.code
                  FROM public.list_schemes AS ls
            INNER JOIN public.locality_schemes AS locs
                    ON ls.id = locs.list_schemes_id                    
                 WHERE ls.code = :scheme
             )
            """

        @Language("PostgreSQL")
        private const val LOCALITY_EXISTS_BY_SCHEME_SQL = """
            SELECT EXISTS(
                SELECT locs.code
                  FROM public.list_schemes AS ls
            INNER JOIN public.locality_schemes AS locs
                    ON ls.id = locs.list_schemes_id
                 WHERE ls.code = :scheme
                   AND locs.code = :locality
           )
        """

        @Language("PostgreSQL")
        private const val LOCALITY_EXISTS_BY_SCHEME_AND_REGION_SQL = """
            SELECT EXISTS(
                SELECT locs.code
                  FROM public.list_schemes AS ls
            INNER JOIN public.locality_schemes AS locs
                    ON ls.id = locs.list_schemes_id
                 WHERE ls.code = :scheme
                   AND locs.region_code = :region
                   AND locs.code = :locality                   
           )
        """

        @Language("PostgreSQL")
        private const val FIND_BY_SQL = """
            SELECT ls.code AS scheme,
                   locs.code AS id,
                   lsi18n.description,
                   ls.uri
              FROM public.list_schemes AS ls
        INNER JOIN public.locality_schemes AS locs
                ON ls.id = locs.list_schemes_id
        INNER JOIN public.locality_schemes_i18n AS lsi18n
                ON locs.id = lsi18n.locality_scheme_id
             WHERE ls.code = :scheme
               AND locs.code = :locality
               AND locs.region_code = :region
               AND lsi18n.language_code = :language
            """
    }

    override fun existsBy(scheme: LocalityScheme): Boolean = jdbcTemplate.queryForObject(
        SCHEME_EXISTS_SQL,
        mapOf(
            "scheme" to scheme.value.toUpperCase()
        ),
        Boolean::class.java
    )!!

    override fun existsBy(scheme: LocalityScheme, locality: LocalityCode): Boolean = jdbcTemplate.queryForObject(
        LOCALITY_EXISTS_BY_SCHEME_SQL,
        mapOf(
            "scheme" to scheme.value.toUpperCase(),
            "locality" to locality.value.toUpperCase()
        ),
        Boolean::class.java
    )!!

    override fun existsBy(scheme: LocalityScheme, locality: LocalityCode, region: RegionCode): Boolean =
        jdbcTemplate.queryForObject(
            LOCALITY_EXISTS_BY_SCHEME_AND_REGION_SQL,
            mapOf(
                "scheme" to scheme.value.toUpperCase(),
                "locality" to locality.value.toUpperCase(),
                "region" to region.value.toUpperCase()
            ),
            Boolean::class.java
        )!!

    override fun findBy(
        scheme: LocalityScheme, locality: LocalityCode, region: RegionCode, language: LanguageCode
    ): LocalityEntity? =
        getObject(
            sql = FIND_BY_SQL,
            params = mapOf(
                "scheme" to scheme.value.toUpperCase(),
                "locality" to locality.value.toUpperCase(),
                "region" to region.value.toUpperCase(),
                "language" to language.value.toUpperCase()
            ),
            mapper = localityRowMapper
        )

    private val localityRowMapper: (ResultSet, Int) -> LocalityEntity = { rs, _ ->
        LocalityEntity(
            scheme = rs.getString("scheme"),
            id = rs.getString("id"),
            description = rs.getString("description"),
            uri = rs.getString("uri")
        )
    }
}