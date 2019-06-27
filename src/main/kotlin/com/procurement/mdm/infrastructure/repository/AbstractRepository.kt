package com.procurement.mdm.infrastructure.repository

import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import java.sql.ResultSet

abstract class AbstractRepository(protected val jdbcTemplate: NamedParameterJdbcTemplate) {
    protected fun <T : Any> getObject(
        sql: String,
        params: Map<String, Any>,
        mapper: (rs: ResultSet, rowNum: Int) -> T
    ): T? = try {
        jdbcTemplate.queryForObject(sql, params, mapper)
    } catch (exception: EmptyResultDataAccessException) {
        null
    }

    protected fun <T : Any> getListObjects(
        sql: String,
        params: Map<String, Any>,
        mapper: (rs: ResultSet, rowNum: Int) -> T
    ): List<T> = jdbcTemplate.query(sql, params, mapper)
}
