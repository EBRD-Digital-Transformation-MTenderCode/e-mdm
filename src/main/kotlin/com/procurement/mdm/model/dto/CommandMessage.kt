package com.procurement.mdm.model.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue
import com.fasterxml.jackson.databind.JsonNode
import com.procurement.mdm.exception.EnumException
import java.util.*

data class CommandMessage @JsonCreator constructor(

        val id: UUID,

        val command: CommandType,

        val context: Context,

        val data: JsonNode?,

        val version: ApiVersion
)

data class Context @JsonCreator constructor(
        val country: String,
        val language: String
)

enum class CommandType(val value: String) {
    TENDER_CPV("tenderCPV"),
    GET_TENDER_DATA("getTenderData");

    @JsonValue
    fun value(): String {
        return this.value
    }

    override fun toString(): String {
        return this.value
    }

    companion object {
        private val CONSTANTS = HashMap<String, CommandType>()

        init {
            for (c in values()) {
                CONSTANTS[c.value] = c
            }
        }

        @JsonCreator
        fun fromValue(value: String): CommandType {
            return CONSTANTS[value] ?: throw EnumException(CommandType::class.java.name, value, Arrays.toString(values()))
        }
    }
}

enum class ApiVersion(val value: String) {
    V_0_1("0.1"),
    V_0_2("0.2");

    @JsonValue
    fun value(): String {
        return this.value
    }

    override fun toString(): String {
        return this.value
    }

    companion object {
        private val CONSTANTS = HashMap<String, ApiVersion>()

        init {
            for (c in ApiVersion.values()) {
                CONSTANTS[c.value] = c
            }
        }

        @JsonCreator
        fun fromValue(value: String): ApiVersion {
            return CONSTANTS[value] ?: throw EnumException(ApiVersion::class.java.name, value, Arrays.toString(CommandType.values()))
        }
    }
}
