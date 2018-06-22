package com.procurement.mdm.repository

import com.procurement.mdm.model.entity.DocumentType
import com.procurement.mdm.model.entity.DtKey
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
interface DocumentTypeRepository : JpaRepository<DocumentType, DtKey> {

    @Transactional(readOnly = true)
    fun findByEntityKindsCodeAndDtKeyLanguageCode(entityKindCode: String = "", languageCode: String): List<DocumentType>
}
