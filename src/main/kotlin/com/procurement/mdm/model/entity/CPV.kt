package com.procurement.mdm.model.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable
import javax.persistence.*

@Embeddable
class CpvKey : Serializable {

    @Column(name = "code", length = 255)
    var code: String? = null

    @JsonIgnore
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = ForeignKey(name = "FK_cpv_language"))
    private val language: Language? = null
}

@Entity
@Table(name = "cpv")
data class CPV(

        @EmbeddedId
        val cpvKey: CpvKey? = null,

        @Column(name = "name")
        val name: String = "",

        @JsonIgnore
        @Column(name = "level")
        val level: Int = 1,

        @JsonIgnore
        @Column(name = "parent")
        val parent: String = "",

        @JsonIgnore
        @Column(name = "description")
        val description: String = ""
)

data class CPVDto(

        @JsonProperty("code")
        val code: String?,

        @JsonProperty("name")
        val name: String?
)

fun List<CPV>.getItems(): List<CPVDto> =
        this.asSequence().map { CPVDto(code = it.cpvKey?.code, name = it.name) }.toList()
