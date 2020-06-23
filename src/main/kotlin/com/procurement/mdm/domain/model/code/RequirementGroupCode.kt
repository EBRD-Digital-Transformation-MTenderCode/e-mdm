package com.procurement.mdm.domain.model.code

import com.procurement.mdm.domain.exception.InvalidRequirementGroupCodeException
import kotlin.jvm.internal.Intrinsics

class RequirementGroupCode private constructor(val value: String) {
    companion object {

        operator fun invoke(value: String): RequirementGroupCode {
            val code = value.trim()
            if (code.isBlank())
                throw InvalidRequirementGroupCodeException("Invalid requirement group code (value is blank).")
            return RequirementGroupCode(code)
        }
    }

    override fun toString(): String = value

    override fun equals(other: Any?): Boolean {
        return if (this !== other) {
            other is RequirementGroupCode && Intrinsics.areEqual(this.value, other.value)
        } else
            true
    }

    override fun hashCode(): Int = value.hashCode()
}
