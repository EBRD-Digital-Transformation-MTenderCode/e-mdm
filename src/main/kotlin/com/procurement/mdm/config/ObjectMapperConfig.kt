package com.procurement.mdm.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.procurement.mdm.infrastructure.bind.jackson.configuration
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration

@Configuration
class ObjectMapperConfig(@Autowired objectMapper: ObjectMapper) {

    init {
        objectMapper.configuration()
    }
}
