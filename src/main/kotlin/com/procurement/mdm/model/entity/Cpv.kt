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

        @Column(name = "level")
        val level: Int = 1,

        @Column(name = "parent")
        val parent: String = "",

        @Column(name = "description")
        val description: String = ""
)

@Embeddable
class CpvKey : Serializable {

    @Column(name = "code", length = 255)
    var code: String? = null

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = ForeignKey(name = "FK_cpv_language"))
    private val language: Language? = null
}

data class CpvDto(

        @JsonProperty("code")
        val code: String?,

        @JsonProperty("name")
        val name: String?
)

fun List<Cpv>.getItems(): List<CpvDto> =
        this.asSequence().map { CpvDto(code = it.cpvKey?.code, name = it.name) }.toList()
