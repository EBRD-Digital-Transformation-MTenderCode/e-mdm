package com.procurement.mdm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication(exclude = {LiquibaseAutoConfiguration.class})
@EnableSwagger2
public class MdmApplication {
    public static void main(String[] args) {
        SpringApplication.run(MdmApplication.class, args);
    }
}
