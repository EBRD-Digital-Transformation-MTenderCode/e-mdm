package com.procurement.mdm.config

import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@Configuration
@ComponentScan(
    basePackages = [
        "com.procurement.mdm.service",
        "com.procurement.mdm.application.service",
        "com.procurement.mdm.domain.service"
    ]
)
class ServiceConfig
