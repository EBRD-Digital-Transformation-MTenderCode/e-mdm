package com.procurement.mdm.repository

import com.procurement.mdm.model.entity.Language
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
interface LanguageRepository : JpaRepository<Language, String> {

    @Transactional(readOnly = true)
    fun findByCode(lang: String): Language?
}

