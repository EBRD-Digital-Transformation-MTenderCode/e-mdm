package com.procurement.mdm.config

import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import springfox.documentation.swagger2.annotations.EnableSwagger2

@Configuration
@EnableSwagger2
@ComponentScan(basePackages = arrayOf("com.procurement.mdm.controller"))
class WebConfig
