package com.procurement.mdm.model.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import java.util.*

@JsonInclude(JsonInclude.Include.NON_NULL)
data class CommandMessage(

        val id: UUID,

        val command: CommandType,


        val items: Any?
)

enum class CommandType(val value: String) {
    CREATE_EI("createEI"),
    CREATE_CN_ON_PIN("createCNonPIN");

    companion object {
        private val CONSTANTS = HashMap<String, CommandType>()

        init {
            for (c in CommandType.values()) {
                CONSTANTS[c.value] = c
            }
        }

        @JsonCreator
        fun fromValue(value: String): CommandType {
            return CONSTANTS[value] ?: throw IllegalArgumentException(value)
        }
    }
}