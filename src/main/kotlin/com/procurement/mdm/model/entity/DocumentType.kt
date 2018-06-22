package com.procurement.mdm.model.entity

import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable
import javax.persistence.*

@Entity
@Table(name = "document_type")
data class DocumentType(

        @EmbeddedId
        val dtKey: DtKey? = null,

        @Column(name = "name")
        val name: String = "",

        @Column(name = "description")
        val description: String = "",

        @ManyToMany(fetch = FetchType.LAZY)
        @JoinTable(name = "document_type_entity_kind",
                joinColumns = [
                    JoinColumn(name = "document_type_code"),
                    JoinColumn(name = "document_type_language_code")],
                inverseJoinColumns = [JoinColumn(name = "entity_kind_code")],
                foreignKey = ForeignKey(name = "FK_document_type_code"),
                inverseForeignKey = ForeignKey(name = "FK_entity_kind_code"))
        private val entityKinds: Set<EntityKind>? = null
)

@Embeddable
class DtKey : Serializable {

    @Column(name = "code", length = 255)
    var code: String? = null

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = ForeignKey(name = "FK_document_type_language"))
    private val language: Language? = null
}

data class DocumentTypeDto(

        @JsonProperty("code")
        val code: String?,

        @JsonProperty("name")
        val name: String?
)

fun List<DocumentType>.getItems(): List<DocumentTypeDto> =
        this.asSequence().map { DocumentTypeDto(code = it.dtKey?.code, name = it.name) }.toList()