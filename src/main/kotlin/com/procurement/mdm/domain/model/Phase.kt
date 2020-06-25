package com.procurement.mdm.domain.model

import com.procurement.mdm.domain.exception.InvalidPhaseException
import kotlin.jvm.internal.Intrinsics

class Phase private constructor(val value: String) {
    companion object {

        operator fun invoke(value: String): Phase {
            val code = value.trim()
            if (code.isBlank())
                throw InvalidPhaseException("Invalid phase (value is blank).")
            return Phase(code)
        }
    }

    override fun toString(): String = value

    override fun equals(other: Any?): Boolean {
        return if (this !== other) {
            other is Phase && Intrinsics.areEqual(this.value, other.value)
        } else
            true
    }

    override fun hashCode(): Int = value.hashCode()
}
