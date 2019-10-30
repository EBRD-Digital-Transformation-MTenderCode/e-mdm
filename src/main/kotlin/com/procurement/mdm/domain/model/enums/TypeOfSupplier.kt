package com.procurement.mdm.domain.model.enums

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue
import com.procurement.mdm.exception.ErrorType
import com.procurement.mdm.exception.InErrorException
import java.util.*

enum class TypeOfSupplier constructor(private val value: String) {
    COMPANY("company"),
    INDIVIDUAL("individual");

    @JsonValue
    fun value(): String {
        return this.value
    }

    override fun toString(): String {
        return this.value
    }

    companion object {
        private val CONSTANTS = HashMap<String, TypeOfSupplier>()

        init {
            for (c in values()) {
                CONSTANTS[c.value] = c
            }
        }

        @JsonCreator
        fun fromValue(value: String): TypeOfSupplier {
            return CONSTANTS[value]
                ?: throw InErrorException(ErrorType.INVALID_JSON_TYPE, TypeOfSupplier::class.java.name)
        }
    }
}