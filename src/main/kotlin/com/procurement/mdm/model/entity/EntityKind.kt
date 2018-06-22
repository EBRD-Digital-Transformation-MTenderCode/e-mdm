package com.procurement.mdm.model.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.*

@Entity
@Table(name = "entity_kind")
data class EntityKind(
        @JsonIgnore
        @Id
        @Column(name = "id")
        val id: String = "",

        @Column(name = "code")
        val code: String = "",

        @Column(name = "name")
        val name: String = "",

        @Column(name = "description")
        val description: String = "",

        @ManyToMany(mappedBy = "entityKinds", fetch = FetchType.LAZY)
        private val documentTypes: Set<DocumentType>? = null
)
