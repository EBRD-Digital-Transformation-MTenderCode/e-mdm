package com.procurement.mdm.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
//@EnableWebMvc
@ComponentScan(basePackages = "com.procurement.mdm.controller")
public class WebConfig{
}
