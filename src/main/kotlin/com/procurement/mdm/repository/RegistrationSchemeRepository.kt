package com.procurement.mdm.repository

import com.procurement.mdm.model.entity.Country
import com.procurement.mdm.model.entity.RegistrationScheme
import com.procurement.mdm.model.entity.RsKey
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
interface RegistrationSchemeRepository : JpaRepository<RegistrationScheme, RsKey> {

    @Transactional(readOnly = true)
    fun findByRsKeyCountry(country: Country): List<RegistrationScheme>
}
