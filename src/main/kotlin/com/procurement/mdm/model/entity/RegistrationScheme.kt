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
        val description: String = ""
)

@Embeddable
class RsKey : Serializable {

    @Column(name = "code", length = 255)
    var code: String? = null

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumns(
            JoinColumn(name = "country_code"),
            JoinColumn(name = "country_language_code"),
            foreignKey = ForeignKey(name = "FK_registration_scheme_country"))
    private val country: Country? = null
}

data class RegistrationSchemeDto(

        @JsonProperty("code")
        val code: String?,

        @JsonProperty("name")
        val name: String?
)

fun List<RegistrationScheme>.getItems(): List<RegistrationSchemeDto> =
        this.asSequence().map { RegistrationSchemeDto(code = it.rsKey?.code, name = it.name) }.toList()