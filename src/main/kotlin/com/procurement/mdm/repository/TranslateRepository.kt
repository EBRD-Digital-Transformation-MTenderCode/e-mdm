package com.procurement.mdm.repository

import com.procurement.mdm.model.entity.Translate
import com.procurement.mdm.model.entity.TranslateKey
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
interface TranslateRepository : JpaRepository<Translate, TranslateKey> {

    @Transactional(readOnly = true)
    fun findByTranslateKeyLanguageCode(languageCode: String): List<Translate>
}
