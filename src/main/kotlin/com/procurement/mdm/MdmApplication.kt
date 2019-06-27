package com.procurement.mdm

import com.procurement.mdm.config.ApplicationConfig
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackageClasses = [ApplicationConfig::class])
class MdmApplication

fun main() {
    runApplication<MdmApplication>()
}
