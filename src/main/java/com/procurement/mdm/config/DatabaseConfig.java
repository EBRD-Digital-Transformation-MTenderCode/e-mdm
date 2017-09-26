package com.procurement.mdm.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@ComponentScan(basePackages = "com.procurement.mdm.model")
@EnableJpaRepositories(basePackages = "com.procurement.mdm.repositories")
@EnableTransactionManagement
public class DatabaseConfig {
}