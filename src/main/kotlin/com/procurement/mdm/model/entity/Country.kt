package com.procurement.mdm.model.entity

import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable
import javax.persistence.*

@Entity
@Table(name = "country")
data class Country(

        @EmbeddedId
        val countryKey: CountryKey? = null,

        @Column(name = "name")
        val name: String = "",

        @Column(name = "description")
        val description: String = "",

        @Column(name = "def")
        val default: Boolean = false,

        @ManyToMany(mappedBy = "countries", fetch = FetchType.LAZY)
        private val currencies: Set<Currency>? = null
)

@Embeddable
class CountryKey : Serializable {

    @Column(name = "code", length = 2)
    var code: String? = null

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = ForeignKey(name = "FK_country_language"))
    private val language: Language? = null
}

data class CountryDto(

        @JsonProperty("code")
        val code: String?,

        @JsonProperty("name")
        val name: String?
)

fun List<Country>.getItems(): List<CountryDto> =
        this.asSequence().map { CountryDto(code = it.countryKey?.code, name = it.name) }.toList()
