package com.procurement.mdm.infrastructure.repository

import com.procurement.mdm.domain.repository.AdvancedLanguageRepository
import com.procurement.mdm.domain.repository.address.AddressCountryRepository
import com.procurement.mdm.domain.repository.address.AddressLocalityRepository
import com.procurement.mdm.domain.repository.address.AddressRegionRepository
import com.procurement.mdm.domain.repository.criterion.CriterionRepository
import com.procurement.mdm.domain.repository.criterion.StandardCriterionRepository
import com.procurement.mdm.domain.repository.organization.OrganizationScaleRepository
import com.procurement.mdm.domain.repository.organization.OrganizationSchemeRepository
import com.procurement.mdm.domain.repository.requirement.RequirementRepository
import com.procurement.mdm.domain.repository.requirement.group.RequirementGroupRepository
import com.procurement.mdm.domain.repository.scheme.CountrySchemeRepository
import com.procurement.mdm.domain.repository.scheme.LocalitySchemeRepository
import com.procurement.mdm.domain.repository.scheme.RegionSchemeRepository
import com.procurement.mdm.infrastructure.repository.address.PostgresAddressCountryRepository
import com.procurement.mdm.infrastructure.repository.address.PostgresAddressLocalityRepository
import com.procurement.mdm.infrastructure.repository.address.PostgresAddressRegionRepository
import com.procurement.mdm.infrastructure.repository.criterion.PostgresCriterionRepository
import com.procurement.mdm.infrastructure.repository.criterion.PostgresStandardCriterionRepository
import com.procurement.mdm.infrastructure.repository.language.PostgresLanguageRepository
import com.procurement.mdm.infrastructure.repository.organization.PostgresOrganizationScaleRepository
import com.procurement.mdm.infrastructure.repository.organization.PostgresOrganizationSchemeRepository
import com.procurement.mdm.infrastructure.repository.requirement.PostgresRequirementRepository
import com.procurement.mdm.infrastructure.repository.requirement.group.PostgresRequirementGroupRepository
import com.procurement.mdm.infrastructure.repository.scheme.PostgresCountrySchemeRepository
import com.procurement.mdm.infrastructure.repository.scheme.PostgresLocalitySchemeRepository
import com.procurement.mdm.infrastructure.repository.scheme.PostgresRegionSchemeRepository
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import liquibase.Liquibase
import liquibase.database.DatabaseFactory
import liquibase.database.jvm.JdbcConnection
import liquibase.resource.FileSystemResourceAccessor
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.datasource.DataSourceTransactionManager
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.annotation.EnableTransactionManagement
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.containers.PostgreSQLContainerProvider
import javax.sql.DataSource

@TestConfiguration
@EnableTransactionManagement
class DatabaseTestConfiguration {
    companion object {
        private const val postgreVersion = "9.6"
        private const val changeLogFile = "liquibase.changelog-master.xml"
    }

    private val postgreSQLContainer = PostgreSQLContainerProvider().newInstance(postgreVersion) as PostgreSQLContainer

    init {
        postgreSQLContainer.start()
    }

    @Bean
    fun transactionManager(): PlatformTransactionManager = DataSourceTransactionManager(dataSource())

    @Bean
    fun dataSource(): DataSource = HikariDataSource(hikariConfig())

    private fun hikariConfig() = HikariConfig().apply {
        jdbcUrl = postgreSQLContainer.getJdbcUrl()
        username = postgreSQLContainer.getUsername()
        password = postgreSQLContainer.getPassword()
        driverClassName = "org.postgresql.Driver"
    }

    @Bean
    fun jdbcTemplate(): NamedParameterJdbcTemplate = NamedParameterJdbcTemplate(dataSource())

    @Bean
    fun advancedLanguageRepository(): AdvancedLanguageRepository =
        PostgresLanguageRepository(jdbcTemplate())

    @Bean
    fun addressCountryRepository(): AddressCountryRepository =
        PostgresAddressCountryRepository(jdbcTemplate())

    @Bean
    fun addressRegionRepository(): AddressRegionRepository =
        PostgresAddressRegionRepository(jdbcTemplate())

    @Bean
    fun regionSchemeRepository(): RegionSchemeRepository =
        PostgresRegionSchemeRepository(jdbcTemplate())

    @Bean
    fun addressLocalityRepository(): AddressLocalityRepository =
        PostgresAddressLocalityRepository(jdbcTemplate())

    @Bean
    fun localitySchemeRepository(): LocalitySchemeRepository =
        PostgresLocalitySchemeRepository(jdbcTemplate())

    @Bean
    fun organizationSchemeRepository(): OrganizationSchemeRepository =
        PostgresOrganizationSchemeRepository(jdbcTemplate())

    @Bean
    fun organizationScaleRepository(): OrganizationScaleRepository =
        PostgresOrganizationScaleRepository(jdbcTemplate())

    @Bean
    fun countrySchemeRepository(): CountrySchemeRepository =
        PostgresCountrySchemeRepository(jdbcTemplate())

    @Bean
    fun criterionRepository(): CriterionRepository =
        PostgresCriterionRepository(jdbcTemplate())

    @Bean
    fun standardCriterionRepository(): StandardCriterionRepository =
        PostgresStandardCriterionRepository(jdbcTemplate())

    @Bean
    fun requirementGroupRepository(): RequirementGroupRepository =
        PostgresRequirementGroupRepository(jdbcTemplate())

    @Bean
    fun requirementRepository(): RequirementRepository =
        PostgresRequirementRepository(jdbcTemplate())

    @Bean
    fun liquibase() = Liquibase(changeLogFile, FileSystemResourceAccessor(liquibaseDir()), database(dataSource()))

    private fun liquibaseDir() = this::class.java
        .getResource("/")
        .path
        .let { path ->
            path.substring(0, path.indexOf("target"))
        }
        .let { baseDir ->
            baseDir + "src/main/resources/liquibase/"
        }

    private fun database(dataSource: DataSource) = DatabaseFactory.getInstance()
        .findCorrectDatabaseImplementation(JdbcConnection(dataSource.connection))
}
