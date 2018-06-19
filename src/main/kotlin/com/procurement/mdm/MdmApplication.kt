package com.procurement.mdm

import com.procurement.mdm.config.ApplicationConfig
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.netflix.eureka.EnableEurekaClient

@SpringBootApplication(scanBasePackageClasses = [ApplicationConfig::class])
@EnableEurekaClient
class MdmApplication

fun main(args: Array<String>) {
    runApplication<MdmApplication>(*args)
}