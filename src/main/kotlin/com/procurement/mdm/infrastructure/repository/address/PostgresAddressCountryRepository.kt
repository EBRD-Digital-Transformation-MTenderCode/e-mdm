package com.procurement.mdm.infrastructure.repository.address

import com.procurement.mdm.domain.entity.CountryEntity
import com.procurement.mdm.domain.model.code.CountryCode
import com.procurement.mdm.domain.model.code.LanguageCode
import com.procurement.mdm.domain.repository.address.AddressCountryRepository
import com.procurement.mdm.infrastructure.repository.AbstractRepository
import org.intellij.lang.annotations.Language
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import java.sql.ResultSet

@Repository
class PostgresAddressCountryRepository(
    jdbcTemplate: NamedParameterJdbcTemplate
) : AbstractRepository(jdbcTemplate), AddressCountryRepository {

    companion object {

        @Language("PostgreSQL")
        private const val COUNTRY_EXISTS_SQL = """
            SELECT EXISTS(
                SELECT code
                  FROM public.address_country_schemes
                 WHERE list_schemes_id = (SELECT list_schemes_id
                                            FROM public.address_country_scheme_used
                                           LIMIT 1)
                   AND code = :code
           )
        """

        @Language("PostgreSQL")
        private const val FIND_ALL_SQL = """
            SELECT ls.code AS scheme,
                   acs.code AS id,
                   acsi18n.description,
                   ls.uri
              FROM public.list_schemes AS ls
        INNER JOIN public.address_country_schemes AS acs
                ON ls.id = acs.list_schemes_id
        INNER JOIN public.address_country_schemes_i18n AS acsi18n
                ON acs.id = acsi18n.address_country_scheme_id
             WHERE ls.id = (SELECT list_schemes_id
                              FROM public.address_country_scheme_used
                             LIMIT 1)
               AND acsi18n.language_code = :language_code
            """

        @Language("PostgreSQL")
        private const val FIND_BY_ID_SQL = """
            SELECT ls.code AS scheme,
                   acs.code AS id,
                   acsi18n.description,
                   ls.uri
              FROM public.list_schemes AS ls
        INNER JOIN public.address_country_schemes AS acs
                ON ls.id = acs.list_schemes_id
        INNER JOIN public.address_country_schemes_i18n AS acsi18n
                ON acs.id = acsi18n.address_country_scheme_id
             WHERE ls.id = (SELECT list_schemes_id
                              FROM public.address_country_scheme_used)
               AND acs.code = :code
               AND acsi18n.language_code = :language_code
            """
    }

    override fun exists(code: CountryCode): Boolean = jdbcTemplate.queryForObject(
        COUNTRY_EXISTS_SQL,
        mapOf(
            "code" to code.value.toUpperCase()
        ),
        Boolean::class.java
    ) ?: throw IllegalStateException("The value of the type is not the boolean.")

    override fun findAll(language: LanguageCode): List<CountryEntity> = getListObjects(
        sql = FIND_ALL_SQL,
        params = mapOf(
            "language_code" to language.value.toUpperCase()
        ),
        mapper = countryRowMapper
    )

    override fun findBy(country: CountryCode, language: LanguageCode): CountryEntity? = getObject(
        sql = FIND_BY_ID_SQL,
        params = mapOf(
            "code" to country.value.toUpperCase(),
            "language_code" to language.value.toUpperCase()
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
