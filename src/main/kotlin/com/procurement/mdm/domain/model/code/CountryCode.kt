package com.procurement.mdm.domain.model.code

import com.procurement.mdm.domain.exception.InvalidCountryCodeException
import kotlin.jvm.internal.Intrinsics

class CountryCode private constructor(val value: String) {
    companion object {
        private const val requiredLength = 2
        operator fun invoke(value: String): CountryCode {
            val code = value.trim()
            if (code.isEmpty())
                throw InvalidCountryCodeException("Invalid country code (value is blank).")
            if (code.length != requiredLength)
                throw InvalidCountryCodeException(
                    "Invalid country code: '$code' (wrong length: '${code.length}' required: '$requiredLength')."
                )
            return CountryCode(code)
        }
    }

    override fun toString(): String = value

    override fun equals(other: Any?): Boolean {
        if (this !== other) {
            if (other is CountryCode) {
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
