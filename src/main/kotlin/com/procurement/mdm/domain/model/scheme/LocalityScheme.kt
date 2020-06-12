package com.procurement.mdm.domain.model.scheme

import com.procurement.mdm.application.exception.SchemeNotFoundException
import com.procurement.mdm.domain.exception.InvalidLocalitySchemeException
import com.procurement.mdm.domain.repository.scheme.LocalitySchemeRepository
import kotlin.jvm.internal.Intrinsics

class LocalityScheme private constructor(val value: String) {
    companion object {
        operator fun invoke(value: String): LocalityScheme {
            val code = value.trim()
            if (code.isEmpty())
                throw InvalidLocalitySchemeException("Invalid locality scheme (value is blank).")
            return LocalityScheme(code)
        }
    }

    fun validation(LocalitySchemeRepository: LocalitySchemeRepository) {
        if (LocalitySchemeRepository.existsBy(this).not())
            throw SchemeNotFoundException(scheme = this)
    }

    override fun toString(): String = value

    override fun equals(other: Any?): Boolean {
        return if (this !== other) {
            other is LocalityScheme && Intrinsics.areEqual(this.value, other.value)
        } else
            true
    }

    override fun hashCode(): Int = value.hashCode()
}
