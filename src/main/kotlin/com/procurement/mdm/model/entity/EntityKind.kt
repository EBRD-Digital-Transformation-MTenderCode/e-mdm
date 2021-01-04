package com.procurement.mdm.model.entity

import com.fasterxml.jackson.annotation.JsonProperty
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.Id
import javax.persistence.ManyToMany
import javax.persistence.Table

@Entity
@Table(name = "entity_kind")
data class EntityKind(
        @Id
        @Column(name = "code")
        val code: String = "",

        @ManyToMany(mappedBy = "entityKinds", fetch = FetchType.LAZY)
        private val documentTypes: List<DocumentType>? = null
)

data class EntityKindDto(

        @JsonProperty("code")
        val code: String?

)

fun List<EntityKind>.getItems(): List<EntityKindDto> =
        this.asSequence().map { EntityKindDto(code = it.code) }.toList()