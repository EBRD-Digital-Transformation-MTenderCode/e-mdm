package com.procurement.mdm.model.entity

import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable
import javax.persistence.*

@Entity
@Table(name = "region")
data class Region(

        @EmbeddedId
        val regionKey: RegionKey? = null,

        @Column(name = "name")
        val name: String = "",

        @Column(name = "description")
        val description: String = "",

        @Column(name = "scheme")
        val scheme: String = "",

        @Column(name = "uri")
        val uri: String = ""
)

@Embeddable
class RegionKey : Serializable {

    @Column(name = "code", length = 255)
    val code: String? = null

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumns(
            JoinColumn(name = "country_code"),
            JoinColumn(name = "country_language_code"),
            foreignKey = ForeignKey(name = "FK_region_country"))
    private val country: Country? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as RegionKey
        if (code != other.code) return false
        if (country != other.country) return false
        return true
    }

    override fun hashCode(): Int {
        var result = code?.hashCode() ?: 0
        result = 31 * result + (country?.hashCode() ?: 0)
        return result
    }
}

data class RegionDto(

        @JsonProperty("code")
        val code: String?,

        @JsonProperty("name")
        val name: String?
)

fun List<Region>.getItems(): List<RegionDto> =
        this.asSequence().map { RegionDto(code = it.regionKey?.code, name = it.name) }.toList()
