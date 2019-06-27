package com.procurement.mdm.domain.model.code

import com.procurement.mdm.domain.exception.InvalidRegionCodeException
import kotlin.jvm.internal.Intrinsics

class RegionCode private constructor(val value: String) {
    companion object {

        operator fun invoke(value: String): RegionCode {
            val code = value.trim()
            if (code.isBlank())
                throw InvalidRegionCodeException("Invalid region code (value is blank).")
            return RegionCode(code)
        }
    }

    override fun toString(): String = value

    override fun equals(other: Any?): Boolean {
        if (this !== other) {
            if (other is RegionCode) {
                if (Intrinsics.areEqual(this.value, other.value)) {
                    return true
                }
            }
            return false
        } else {
            return true
        }
    }

    override fun hashCode(): Int = value.hashCode()
}
