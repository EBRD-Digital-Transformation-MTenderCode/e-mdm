package com.procurement.mdm.domain.model.code

import com.procurement.mdm.domain.exception.InvalidLocalityCodeException
import kotlin.jvm.internal.Intrinsics

class LocalityCode private constructor(val value: String) {
    companion object {

        operator fun invoke(value: String): LocalityCode {
            val code = value.trim()
            if (code.isBlank())
                throw InvalidLocalityCodeException("Invalid locality code (value is blank).")
            return LocalityCode(code)
        }
    }

    override fun toString(): String = value

    override fun equals(other: Any?): Boolean {
        return if (this !== other) {
            other is LocalityCode && Intrinsics.areEqual(this.value, other.value)
        } else
            true
    }

    override fun hashCode(): Int = value.hashCode()
}
