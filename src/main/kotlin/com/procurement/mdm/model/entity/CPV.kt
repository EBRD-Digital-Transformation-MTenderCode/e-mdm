package com.procurement.mdm.model.entity

import com.fasterxml.jackson.annotation.JsonIgnore

import javax.persistence.*

@Entity
@Table(name = "cpv")
data class CPV(

        @EmbeddedId
        val cpvIdentity: CpvIdentity? = null,

        @Column(name = "name")
        val name: String = "",

        @JsonIgnore
        @Column(name = "level")
        val level: Int = 1,

        @JsonIgnore
        @Column(name = "parent")
        val parent: String = "",

        @JsonIgnore
        @Column(name = "description")
        val description: String = ""
)
