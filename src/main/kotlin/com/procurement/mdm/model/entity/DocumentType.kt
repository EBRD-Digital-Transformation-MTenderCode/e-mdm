package com.procurement.mdm.model.entity

import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Embeddable
import javax.persistence.EmbeddedId
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.ForeignKey
import javax.persistence.JoinColumn
import javax.persistence.JoinTable
import javax.persistence.ManyToMany
import javax.persistence.ManyToOne
import javax.persistence.Table

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
        private val entityKinds: List<EntityKind>? = null
)

@Embeddable
class DtKey : Serializable {

    @Column(name = "code", length = 255)
    val code: String? = null

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = ForeignKey(name = "FK_document_type_language"))
    private val language: Language? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as DtKey
        if (code != other.code) return false
        if (language != other.language) return false
        return true
    }

    override fun hashCode(): Int {
        var result = code?.hashCode() ?: 0
        result = 31 * result + (language?.hashCode() ?: 0)
        return result
    }
}

data class DocumentTypeDto(

        @JsonProperty("code")
        val code: String?,

        @JsonProperty("name")
        val name: String?
)

fun List<DocumentType>.getItems(): List<DocumentTypeDto> =
        this.asSequence().map { DocumentTypeDto(code = it.dtKey?.code, name = it.name) }.toList()