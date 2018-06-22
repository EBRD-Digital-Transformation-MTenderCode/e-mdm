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
        val description: String = ""
)

@Embeddable
class RegionKey : Serializable {

    @Column(name = "code", length = 255)
    var code: String? = null

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumns(
            JoinColumn(name = "country_code"),
            JoinColumn(name = "country_language_code"),
            foreignKey = ForeignKey(name = "FK_region_country"))
    private val country: Country? = null
}

data class RegionDto(

        @JsonProperty("code")
        val code: String?,

        @JsonProperty("name")
        val name: String?
)

fun List<Region>.getItems(): List<RegionDto> =
        this.asSequence().map { RegionDto(code = it.regionKey?.code, name = it.name) }.toList()
