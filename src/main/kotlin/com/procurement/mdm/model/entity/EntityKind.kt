package com.procurement.mdm.model.entity

import javax.persistence.*

@Entity
@Table(name = "entity_kind")
data class EntityKind(
        @Id
        @Column(name = "code")
        val code: String = "",

        @Column(name = "name")
        val name: String = "",

        @Column(name = "description")
        val description: String = "",

        @ManyToMany(mappedBy = "entityKinds", fetch = FetchType.LAZY)
        private val documentTypes: Set<DocumentType>? = null
)
