package com.procurement.mdm.infrastructure.repository.scheme

import com.procurement.mdm.domain.entity.CountryEntity
import com.procurement.mdm.domain.model.code.CountryCode
import com.procurement.mdm.domain.model.code.LanguageCode
import com.procurement.mdm.domain.model.scheme.CountryScheme
import com.procurement.mdm.domain.repository.scheme.CountrySchemeRepository
import com.procurement.mdm.infrastructure.repository.AbstractRepository
import org.intellij.lang.annotations.Language
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import java.sql.ResultSet

@Repository
class PostgresCountrySchemeRepository(
    jdbcTemplate: NamedParameterJdbcTemplate
) : AbstractRepository(jdbcTemplate), CountrySchemeRepository {

    companion object {
        @Language("PostgreSQL")
        private const val SCHEME_EXISTS_SQL = """
            SELECT EXISTS(
                SELECT ls.code
                  FROM public.list_schemes AS ls
            INNER JOIN public.country_schemes AS cs
                    ON ls.id = cs.list_schemes_id                    
                 WHERE ls.code = :scheme
             )
            """

        @Language("PostgreSQL")
        private const val COUNTRY_EXISTS_BY_SCHEME_SQL = """
            SELECT EXISTS(
                SELECT cs.code
                  FROM public.list_schemes AS ls
            INNER JOIN public.country_schemes AS cs
                    ON ls.id = cs.list_schemes_id                    
                 WHERE ls.code = :scheme
                   AND cs.code = :country
             )
            """

        @Language("PostgreSQL")
        private const val FIND_BY_COUNTRY_SCHEME_AND_LANGUAGE_SQL = """
            SELECT ls.code AS scheme,
                   cs.code AS id,
                   csi18n.description,
                   ls.uri
              FROM public.list_schemes AS ls
        INNER JOIN public.country_schemes AS cs
                ON ls.id = cs.list_schemes_id
        INNER JOIN public.country_schemes_i18n AS csi18n
                ON cs.id = csi18n.country_scheme_id
             WHERE ls.code = :scheme
               AND cs.code = :country
               AND csi18n.language_code = :language
            """
    }

    override fun existsBy(scheme: CountryScheme): Boolean = jdbcTemplate.queryForObject(
        SCHEME_EXISTS_SQL,
        mapOf(
            "scheme" to scheme.value.toUpperCase()
        ),
        Boolean::class.java
    )!!

    override fun existsBy(scheme: CountryScheme, country: CountryCode): Boolean = jdbcTemplate.queryForObject(
        COUNTRY_EXISTS_BY_SCHEME_SQL,
        mapOf(
            "scheme" to scheme.value.toUpperCase(),
            "country" to country.value.toUpperCase()
        ),
        Boolean::class.java
    )!!

    override fun findBy(scheme: CountryScheme, country: CountryCode, language: LanguageCode): CountryEntity? = getObject(
        sql = FIND_BY_COUNTRY_SCHEME_AND_LANGUAGE_SQL,
        params = mapOf(
            "country" to country.value.toUpperCase(),
            "language" to language.value.toUpperCase(),
            "scheme" to scheme.value.toUpperCase()
        ),
        mapper = countryRowMapper
    )

    private val countryRowMapper: (ResultSet, Int) -> CountryEntity = { rs, _ ->
        CountryEntity(
            scheme = rs.getString("scheme"),
            id = rs.getString("id"),
            description = rs.getString("description"),
            uri = rs.getString("uri")
        )
    }
}