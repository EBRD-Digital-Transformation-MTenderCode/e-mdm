package com.procurement.mdm.model.entity

import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable
import javax.persistence.*

@Entity
@Table(name = "cpvs")
data class Cpvs(

        @EmbeddedId
        val cpvsKey: CpvsKey? = null,

        @Column(name = "name")
        val name: String = "",

        @Column(name = "level")
        val level: Int = 1,

        @Column(name = "parent")
        val parent: String = "",

        @Column(name = "description")
        val description: String = ""
)

@Embeddable
class CpvsKey : Serializable {

    @Column(name = "code", length = 255)
    var code: String? = null

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = ForeignKey(name = "FK_cpvs_language"))
    private val language: Language? = null
}

data class CpvsDto(

        @JsonProperty("code")
        val code: String?,

        @JsonProperty("name")
        val name: String?
)

fun List<Cpvs>.getItems(): List<CpvsDto> =
        this.asSequence().map { CpvsDto(code = it.cpvsKey?.code, name = it.name) }.toList()
