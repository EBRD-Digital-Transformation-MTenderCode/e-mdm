package com.procurement.mdm.config

import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration
@Import(DatabaseConfig::class, ServiceConfig::class, WebConfig::class, ObjectMapperConfig::class)
class ApplicationConfig
