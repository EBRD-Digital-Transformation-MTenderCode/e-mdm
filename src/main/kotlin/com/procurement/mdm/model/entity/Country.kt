package com.procurement.mdm.model.entity

import com.fasterxml.jackson.annotation.JsonIgnore

import javax.persistence.*

@Entity
@Table(name = "countries")
data class Country(
        @Id
        @Column(name = "code")
        val code: String = "",

        @Column(name = "name")
        val name: String = "",

        @Column(name = "description")
        val description: String = "",

        @Column(name = "def")
        val default: Boolean = false,

        @JsonIgnore
        @ManyToOne(optional = false, fetch = FetchType.LAZY)
        @JoinColumn(foreignKey = ForeignKey(name = "FK_countries_language"))
        private val language: Language? = null
)
