package com.procurement.mdm.domain.model.scheme

import com.procurement.mdm.application.exception.RegionSchemeNotFoundException
import com.procurement.mdm.domain.exception.InvalidRegionSchemeException
import com.procurement.mdm.domain.repository.scheme.RegionSchemeRepository
import kotlin.jvm.internal.Intrinsics

class RegionScheme private constructor(val value: String) {
    companion object {
        operator fun invoke(value: String): RegionScheme {
            val code = value.trim()
            if (code.isEmpty())
                throw InvalidRegionSchemeException("Invalid region scheme (value is blank).")
            return RegionScheme(code)
        }
    }

    fun validation(regionSchemeRepository: RegionSchemeRepository) {
        if (regionSchemeRepository.existsBy(this).not())
            throw RegionSchemeNotFoundException(scheme = this)
    }

    override fun toString(): String = value

    override fun equals(other: Any?): Boolean {
        return if (this !== other) {
            other is RegionScheme && Intrinsics.areEqual(this.value, other.value)
        } else
            true
    }

    override fun hashCode(): Int = value.hashCode()
}
