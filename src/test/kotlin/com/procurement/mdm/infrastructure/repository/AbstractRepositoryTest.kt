package com.procurement.mdm.infrastructure.repository

import liquibase.Contexts
import liquibase.Liquibase
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import javax.sql.DataSource

@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [DatabaseTestConfiguration::class])
open class AbstractRepositoryTest {

    @Autowired
    protected lateinit var datasource: DataSource

    @Autowired
    protected lateinit var liquibase: Liquibase

    private val executor: (String) -> Unit by lazyOf { sql -> JdbcTemplate(datasource).execute(sql) }

    @BeforeEach
    protected open fun initDatabase() {
        liquibase.update(Contexts("test"))
    }

    @AfterEach
    protected open fun clearDatabase() {
        liquibase.dropAll()
    }

    protected fun executeSQLScript(sql: String) = executor(sql)
}
