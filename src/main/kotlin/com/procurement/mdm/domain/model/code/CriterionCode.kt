package com.procurement.mdm.domain.model.code

import com.procurement.mdm.domain.exception.InvalidCriterionCodeException
import kotlin.jvm.internal.Intrinsics

class CriterionCode private constructor(val value: String) {
    companion object {

        operator fun invoke(value: String): CriterionCode {
            val code = value.trim()
            if (code.isBlank())
                throw InvalidCriterionCodeException("Invalid criteria code (value is blank).")
            return CriterionCode(code)
        }
    }

    override fun toString(): String = value

    override fun equals(other: Any?): Boolean {
        return if (this !== other) {
            other is CriterionCode && Intrinsics.areEqual(this.value, other.value)
        } else
            true
    }

    override fun hashCode(): Int = value.hashCode()
}
