package com.procurement.mdm.infrastructure.repository.address

import com.procurement.mdm.domain.entity.LocalityEntity
import com.procurement.mdm.domain.model.code.CountryCode
import com.procurement.mdm.domain.model.code.LanguageCode
import com.procurement.mdm.domain.model.code.LocalityCode
import com.procurement.mdm.domain.model.code.RegionCode
import com.procurement.mdm.domain.repository.address.AddressLocalityRepository
import com.procurement.mdm.infrastructure.repository.AbstractRepository
import org.intellij.lang.annotations.Language
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import java.sql.ResultSet

@Repository
class PostgresAddressLocalityRepository(
    jdbcTemplate: NamedParameterJdbcTemplate
) : AbstractRepository(jdbcTemplate), AddressLocalityRepository {

    companion object {
        @Language("PostgreSQL")
        private const val FIND_ALL_SQL = """
            SELECT ls.code AS scheme,
                   als.code AS id,
                   alsi18n.description,
                   ls.uri
              FROM public.list_schemes AS ls
        INNER JOIN public.address_locality_schemes AS als
                ON ls.id = als.list_schemes_id
        INNER JOIN public.address_locality_schemes_i18n AS alsi18n
                ON als.id = alsi18n.address_locality_schemes_id
             WHERE ls.id = (SELECT list_schemes_id
                              FROM public.address_locality_scheme_used
                             WHERE country_code = :country_code
                               AND region_code = :region_code)
               AND als.country_code = :country_code
               AND als.region_code = :region_code
               AND alsi18n.language_code = :language_code
            """

        @Language("PostgreSQL")
        private const val FIND_BY_ID_SQL = """
            SELECT ls.code AS scheme,
                   als.code AS id,
                   alsi18n.description,
                   ls.uri
              FROM public.list_schemes AS ls
        INNER JOIN public.address_locality_schemes AS als
                ON ls.id = als.list_schemes_id
        INNER JOIN public.address_locality_schemes_i18n AS alsi18n
                ON als.id = alsi18n.address_locality_schemes_id
             WHERE ls.id = (SELECT list_schemes_id
                              FROM public.address_locality_scheme_used
                             WHERE country_code = :country_code
                               AND region_code = :region_code)
               AND als.code = :code
               AND als.country_code = :country_code
               AND als.region_code = :region_code
               AND alsi18n.language_code = :language_code
            """

        @Language("PostgreSQL")
        private const val GET_ALL_SCHEMES_SQL = """
            SELECT ls.code AS scheme
              FROM public.list_schemes AS ls
        INNER JOIN public.address_locality_scheme_used AS alsu
                ON ls.id = alsu.list_schemes_id
             WHERE alsu.country_code = :country_code
               AND alsu.region_code = :region_code
            """
    }

    override fun findAll(country: CountryCode, region: RegionCode, language: LanguageCode): List<LocalityEntity> =
        getListObjects(
            sql = FIND_ALL_SQL,
            params = mapOf(
                "country_code" to country.value.toUpperCase(),
                "region_code" to region.value.toUpperCase(),
                "language_code" to language.value.toUpperCase()
            ),
            mapper = localityRowMapper
        )

    override fun findBy(
        locality: LocalityCode,
        country: CountryCode,
        region: RegionCode,
        language: LanguageCode
    ): LocalityEntity? = getObject(
        sql = FIND_BY_ID_SQL,
        params = mapOf(
            "code" to locality.value.toUpperCase(),
            "country_code" to country.value.toUpperCase(),
            "region_code" to region.value.toUpperCase(),
            "language_code" to language.value.toUpperCase()
        ),
        mapper = localityRowMapper
    )

    override fun findAllSchemes(country: CountryCode, region: RegionCode): List<String> = getListObjects(
        sql = GET_ALL_SCHEMES_SQL,
        params = mapOf(
            "country_code" to country.value.toUpperCase(),
            "region_code" to region.value.toUpperCase()
        ),
        mapper = schemeRowMapper
    )

    private val localityRowMapper: (ResultSet, Int) -> LocalityEntity = { rs, _ ->
        LocalityEntity(
            scheme = rs.getString("scheme"),
            id = rs.getString("id"),
            description = rs.getString("description"),
            uri = rs.getString("uri")
        )
    }

    private val schemeRowMapper: (ResultSet, Int) -> String = { rs, _ ->
        rs.getString("scheme")
    }
}
