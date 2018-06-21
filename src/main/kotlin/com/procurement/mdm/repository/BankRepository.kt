package com.procurement.mdm.repository

import com.procurement.mdm.model.entity.Bank
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
interface BankRepository : JpaRepository<Bank, String> {

    @Transactional(readOnly = true)
    fun findByCountry(country: String): List<Bank>
}

