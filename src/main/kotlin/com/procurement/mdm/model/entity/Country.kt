package com.procurement.mdm.model.entity

import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Embeddable
import javax.persistence.EmbeddedId
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.ForeignKey
import javax.persistence.JoinColumn
import javax.persistence.ManyToMany
import javax.persistence.ManyToOne
import javax.persistence.Table

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

        @Column(name = "scheme")
        val scheme: String = "",

        @Column(name = "uri")
        val uri: String = "",

        @ManyToMany(mappedBy = "countries", fetch = FetchType.LAZY)
        private val currencies: List<Currency>? = null
)

@Embeddable
class CountryKey : Serializable {

    @Column(name = "code", length = 2)
    val code: String? = null

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = ForeignKey(name = "FK_country_language"))
    private val language: Language? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as CountryKey
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

data class CountryDto(

        @JsonProperty("code")
        val code: String?,

        @JsonProperty("name")
        val name: String?
)

fun List<Country>.getItems(): List<CountryDto> =
        this.asSequence().map { CountryDto(code = it.countryKey?.code, name = it.name) }.toList()
