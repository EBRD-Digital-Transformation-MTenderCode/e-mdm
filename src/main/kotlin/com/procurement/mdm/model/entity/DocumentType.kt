package com.procurement.mdm.model.entity

import com.fasterxml.jackson.annotation.JsonIgnore

import javax.persistence.*

@Entity
@Table(name = "document_type")
data class DocumentType(

        @Id
        @Column(name = "id")
        val id: String = "",

        @Column(name = "code")
        val code: String = "",

        @Column(name = "name")
        val name: String = "",

        @JsonIgnore
        @Column(name = "description")
        val description: String = "",

        @JsonIgnore
        @ManyToOne(optional = false, fetch = FetchType.LAZY)
        @JoinColumn(foreignKey = ForeignKey(name = "FK_document_type_language"))
        private val language: Language? = null,

        @JsonIgnore
        @ManyToMany(fetch = FetchType.LAZY)
        @JoinTable(name = "document_type_entity_kind",
                joinColumns = [JoinColumn(name = "document_type_code")],
                inverseJoinColumns = [JoinColumn(name = "entity_kind_code")],
                foreignKey = ForeignKey(name = "FK_document_type_code"),
                inverseForeignKey = ForeignKey(name = "FK_entity_kind_code"))
        private val entityKinds: Set<EntityKind>? = null
)
