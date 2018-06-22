package com.procurement.mdm.repository

import com.procurement.mdm.model.entity.DocumentType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
interface DocumentTypeRepository : JpaRepository<DocumentType, String> {

    @Transactional(readOnly = true)
    fun findByLanguageIdAndEntityKindsId(languageId: String, entityKindId: String): List<DocumentType>
}
