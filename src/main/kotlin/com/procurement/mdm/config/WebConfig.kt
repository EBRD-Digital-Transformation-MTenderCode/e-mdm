package com.procurement.mdm.config

import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import springfox.documentation.swagger2.annotations.EnableSwagger2

@Configuration
@EnableSwagger2
@ComponentScan(
    basePackages = [
        "com.procurement.mdm.controller",
        "com.procurement.mdm.infrastructure.web.controller"
    ]
)
class WebConfig
