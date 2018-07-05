package com.procurement.mdm.model.entity

import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable
import javax.persistence.*

@Entity
@Table(name = "submission_method_rationale")
data class SubMetRat(

        @EmbeddedId
        val subMetRatKey: SubMetRatKey? = null,

        @Column(name = "name")
        val name: String = "",

        @Column(name = "description")
        val description: String = ""
)

@Embeddable
class SubMetRatKey : Serializable {

    @Column(name = "code", length = 255)
    val code: String? = null

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = ForeignKey(name = "FK_submission_method_rationale_language"))
    private val language: Language? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as SubMetRatKey
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

data class SubMetRatDto(

        @JsonProperty("code")
        val code: String?,

        @JsonProperty("name")
        val name: String?
)

fun List<SubMetRat>.getItems(): List<SubMetRatDto> =
        this.asSequence().map { SubMetRatDto(code = it.subMetRatKey?.code, name = it.name) }.toList()
