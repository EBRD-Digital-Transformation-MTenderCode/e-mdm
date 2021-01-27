package com.procurement.mdm.domain.model.criterion

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue
import com.procurement.mdm.exception.ErrorType
import com.procurement.mdm.exception.InErrorException

enum class CriterionCategory(@JsonValue val key: String) {

    EXCLUSION("CRITERION.EXCLUSION."),
    SELECTION("CRITERION.SELECTION."),
    OTHER("CRITERION.OTHER.");

    override fun toString(): String = key

    companion object {
        private val CONSTANTS: Map<String, CriterionCategory> = values().associateBy { it.key.toUpperCase() }

        @JvmStatic
        @JsonCreator
        fun fromValue(value: String): CriterionCategory {
            return CONSTANTS[value.toUpperCase()]
                ?: throw InErrorException(ErrorType.INVALID_JSON_TYPE, CriterionCategory::class.java.name)
        }
    }
}