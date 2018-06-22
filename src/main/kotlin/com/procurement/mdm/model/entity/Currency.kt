package com.procurement.mdm.model.entity

import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable
import javax.persistence.*

@Entity
@Table(name = "currency")
data class Currency(

        @EmbeddedId
        val currencyKey: CurrencyKey? = null,

        @Column(name = "name")
        val name: String = "",

        @Column(name = "def")
        val default: Boolean = false,

        @Column(name = "description")
        val description: String = "",

        @ManyToMany(fetch = FetchType.LAZY)
        @JoinTable(name = "currency_country",
                joinColumns = [
                    JoinColumn(name = "currency_code"),
                    JoinColumn(name = "currency_language_code")],
                inverseJoinColumns = [
                    JoinColumn(name = "country_code"),
                    JoinColumn(name = "country_language_code")],
                foreignKey = ForeignKey(name = "FK_currency"),
                inverseForeignKey = ForeignKey(name = "FK_country"))
        private val countries: Set<Country>? = null
)

@Embeddable
class CurrencyKey : Serializable {

    @Column(name = "code", length = 3)
    val code: String? = null

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = ForeignKey(name = "FK_currency_language"))
    private val language: Language? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as CurrencyKey
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

data class CurrencyDto(

        @JsonProperty("code")
        val code: String?,

        @JsonProperty("name")
        val name: String?
)

fun List<Currency>.getItems(): List<CurrencyDto> =
        this.asSequence().map { CurrencyDto(code = it.currencyKey?.code, name = it.name) }.toList()
