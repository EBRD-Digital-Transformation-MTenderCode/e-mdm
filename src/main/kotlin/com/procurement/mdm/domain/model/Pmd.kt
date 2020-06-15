package com.procurement.mdm.domain.model

import com.procurement.mdm.domain.exception.InvalidPmdException
import kotlin.jvm.internal.Intrinsics

class Pmd private constructor(val value: String) {
    companion object {

        operator fun invoke(value: String): Pmd {
            val code = value.trim()
            if (code.isBlank())
                throw InvalidPmdException("Invalid pmd (value is blank).")
            return Pmd(code)
        }
    }

    override fun toString(): String = value

    override fun equals(other: Any?): Boolean {
        return if (this !== other) {
            other is Pmd && Intrinsics.areEqual(this.value, other.value)
        } else
            true
    }

    override fun hashCode(): Int = value.hashCode()
}
