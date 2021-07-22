package com.procurement.mdm.infrastructure.bind.jackson

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.node.JsonNodeFactory
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.procurement.mdm.model.dto.databinding.IntDeserializer
import com.procurement.mdm.model.dto.databinding.JsonDateDeserializer
import com.procurement.mdm.model.dto.databinding.JsonDateSerializer
import com.procurement.mdm.model.dto.databinding.StringsDeserializer
import java.time.LocalDateTime

fun ObjectMapper.configuration() {

    val module = SimpleModule()
        .apply {
            addSerializer(LocalDateTime::class.java, JsonDateSerializer())
            addDeserializer(LocalDateTime::class.java, JsonDateDeserializer())
            addDeserializer(String::class.java, StringsDeserializer())
            addDeserializer(Int::class.java, IntDeserializer())
        }

    registerModule(module)
    registerKotlinModule()

    configure(DeserializationFeature.USE_BIG_INTEGER_FOR_INTS, true)
    configure(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS, true)
    configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    configure(DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS, true)
    configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    nodeFactory = JsonNodeFactory.withExactBigDecimals(true)
}
