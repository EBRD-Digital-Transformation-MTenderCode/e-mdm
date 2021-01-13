package com.procurement.mdm.domain.model

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue
import com.procurement.mdm.exception.ErrorType
import com.procurement.mdm.exception.InErrorException

enum class ProcurementCategory(@JsonValue val key: String) {
    GOODS("goods"),
    WORKS("works"),
    SERVICES("services");

    override fun toString(): String = key

    companion object {
        private val CONSTANTS: Map<String, ProcurementCategory> = values().associateBy { it.key.toUpperCase() }

        @JvmStatic
        @JsonCreator
        fun fromValue(value: String): ProcurementCategory {
            return CONSTANTS[value.toUpperCase()]
                ?: throw InErrorException(ErrorType.INVALID_JSON_TYPE, ProcurementCategory::class.java.name)
        }
    }
}
