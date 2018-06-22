package com.procurement.mdm.model.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import java.io.Serializable
import javax.persistence.*
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size


@Embeddable
class CpvIdentity : Serializable {
    @NotNull
    @Size(max = 255)
    var code: String? = null

    @JsonIgnore
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = ForeignKey(name = "FK_cpv_language"))
    private val language: Language? = null
}
