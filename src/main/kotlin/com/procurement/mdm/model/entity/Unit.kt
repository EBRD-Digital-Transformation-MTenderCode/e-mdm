package com.procurement.mdm.model.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable

import javax.persistence.*

@Entity
@Table(name = "unit")
data class Unit(

        @EmbeddedId
        val unitKey: UnitKey? = null,

        @Column(name = "name")
        val name: String = "",

        @JsonIgnore
        @Column(name = "description")
        val description: String = "",


        @JsonIgnore
        @ManyToOne(optional = false, fetch = FetchType.LAZY)
        @JoinColumn(foreignKey = ForeignKey(name = "FK_unit_unit_class"))
        private val unitClass: UnitClass? = null
)

@Embeddable
class UnitKey : Serializable {

    @Column(name = "code", length = 255)
    var code: String? = null

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = ForeignKey(name = "FK_unit_language"))
    private val language: Language? = null
}

data class UnitDto(

        @JsonProperty("code")
        val code: String?,

        @JsonProperty("name")
        val name: String?
)

fun List<Unit>.getItems(): List<UnitDto> =
        this.asSequence().map { UnitDto(code = it.unitKey?.code, name = it.name) }.toList()