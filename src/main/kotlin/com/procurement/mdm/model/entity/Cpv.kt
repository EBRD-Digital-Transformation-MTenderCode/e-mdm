package com.procurement.mdm.model.entity

import com.fasterxml.jackson.annotation.JsonIgnore

import javax.persistence.*

@Entity
@Table(name = "cpv")
data class Cpv(

        @Id
        @Column(name = "code")
        val code: String = "",

        @Column(name = "name")
        val name: String = "",

        @Column(name = "level")
        val level: Int = 1,

        @Column(name = "parent")
        val parent: String = "",

        @JsonIgnore
        @ManyToOne(optional = false, fetch = FetchType.LAZY)
        @JoinColumn(foreignKey = ForeignKey(name = "FK_cpv_language"))
        private val language: Language? = null
)
