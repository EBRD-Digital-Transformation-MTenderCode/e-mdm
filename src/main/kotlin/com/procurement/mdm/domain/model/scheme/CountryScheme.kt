package com.procurement.mdm.domain.model.scheme

import com.procurement.mdm.application.exception.SchemeNotFoundException
import com.procurement.mdm.domain.exception.InvalidCountrySchemeException
import com.procurement.mdm.domain.repository.scheme.CountrySchemeRepository
import kotlin.jvm.internal.Intrinsics

class CountryScheme private constructor(val value: String) {
    companion object {
        operator fun invoke(value: String): CountryScheme {
            val code = value.trim()
            if (code.isEmpty())
                throw InvalidCountrySchemeException("Invalid country scheme (value is blank).")
            return CountryScheme(code)
        }
    }

    fun validation(countrySchemeRepository: CountrySchemeRepository) {
        if (countrySchemeRepository.existsBy(this).not())
            throw SchemeNotFoundException(scheme = this)
    }

    override fun toString(): String = value

    override fun equals(other: Any?): Boolean {
        return if (this !== other) {
            other is CountryScheme && Intrinsics.areEqual(this.value, other.value)
        } else
            true
    }

    override fun hashCode(): Int = value.hashCode()
}
