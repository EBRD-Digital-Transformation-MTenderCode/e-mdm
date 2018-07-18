package com.procurement.mdm.model.entity

import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable
import javax.persistence.*

@Entity
@Table(name = "registration_scheme")
data class RegistrationScheme(

        @EmbeddedId
        val rsKey: RsKey? = null,

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
class RsKey : Serializable {

    @Column(name = "code", length = 255)
    val code: String? = null

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumns(
            JoinColumn(name = "country_code"),
            JoinColumn(name = "country_language_code"),
            foreignKey = ForeignKey(name = "FK_registration_scheme_country"))
    private val country: Country? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as RsKey
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

data class RegistrationSchemeDto(

        @JsonProperty("code")
        val code: String?,

        @JsonProperty("name")
        val name: String?
)

fun List<RegistrationScheme>.getItems(): List<RegistrationSchemeDto> =
        this.asSequence().map { RegistrationSchemeDto(code = it.rsKey?.code, name = it.name) }.toList()