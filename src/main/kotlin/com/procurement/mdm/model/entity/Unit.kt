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
        val description: String = ""
)


@Embeddable
class UnitKey : Serializable {

    @Column(name = "code", length = 255)
    val code: String? = null

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumns(
            JoinColumn(name = "unit_class_code"),
            JoinColumn(name = "unit_class_language_code"),
            foreignKey = ForeignKey(name = "FK_unit_unit_class"))
    private val unitClass: UnitClass? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as UnitKey
        if (code != other.code) return false
        if (unitClass != other.unitClass) return false
        return true
    }

    override fun hashCode(): Int {
        var result = code?.hashCode() ?: 0
        result = 31 * result + (unitClass?.hashCode() ?: 0)
        return result
    }
}

data class UnitDto(

        @JsonProperty("code")
        val code: String?,

        @JsonProperty("name")
        val name: String?
)

fun List<Unit>.getItems(): List<UnitDto> =
        this.asSequence().map { UnitDto(code = it.unitKey?.code, name = it.name) }.toList()