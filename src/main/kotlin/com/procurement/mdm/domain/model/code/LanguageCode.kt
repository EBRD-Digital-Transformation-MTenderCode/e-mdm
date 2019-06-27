package com.procurement.mdm.domain.model.code

import com.procurement.mdm.domain.exception.InvalidLanguageCodeException
import com.procurement.mdm.domain.exception.LanguageUnknownException
import com.procurement.mdm.domain.repository.AdvancedLanguageRepository
import kotlin.jvm.internal.Intrinsics

class LanguageCode private constructor(val value: String) {
    companion object {
        private const val requiredLength = 2
        operator fun invoke(value: String): LanguageCode {
            val code = value.trim()
            if (code.isBlank())
                throw InvalidLanguageCodeException("Invalid language code (value is blank).")
            if (code.length != requiredLength)
                throw InvalidLanguageCodeException(
                    "Invalid language code: '$code' (wrong length: '${code.length}' required: '$requiredLength')."
                )
            return LanguageCode(code)
        }
    }

    fun validation(advancedLanguageRepository: AdvancedLanguageRepository) {
        if (advancedLanguageRepository.exists(this).not())
            throw LanguageUnknownException(language = this.value)
    }

    override fun toString(): String = value

    override fun equals(other: Any?): Boolean {
        if (this !== other) {
            if (other is LanguageCode) {
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
