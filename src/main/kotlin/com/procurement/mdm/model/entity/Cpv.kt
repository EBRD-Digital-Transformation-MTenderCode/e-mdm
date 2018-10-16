package com.procurement.mdm.model.entity

import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable
import javax.persistence.*

@Entity
@Table(name = "cpv")
data class Cpv(

        @EmbeddedId
        val cpvKey: CpvKey? = null,

        @Column(name = "name")
        val name: String = "",

        @Column(name = "parent")
        val parent: String = "",

        @Column(name = "description")
        val description: String = "",

        @Column(name = "procurement_category")
        val mainProcurementCategory: String = ""
)

@Embeddable
class CpvKey(

        @Column(name = "code", length = 255)
        val code: String? = null,

        @ManyToOne(optional = false, fetch = FetchType.LAZY)
        @JoinColumn(foreignKey = ForeignKey(name = "FK_cpv_language"))
        private val language: Language? = null

) : Serializable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as CpvKey
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

data class CpvDto(

        @JsonProperty("code")
        val code: String?,

        @JsonProperty("name")
        val name: String?
)

fun List<Cpv>.getItems(): List<CpvDto> =
        this.asSequence().map { CpvDto(code = it.cpvKey?.code, name = it.name) }.toList()
