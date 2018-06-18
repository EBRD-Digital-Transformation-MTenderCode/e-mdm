package com.procurement.mdm.config

import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@Configuration
@ComponentScan(basePackages = arrayOf("com.procurement.mdm.service"))
class ServiceConfig
