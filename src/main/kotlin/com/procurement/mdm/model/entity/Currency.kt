package com.procurement.mdm.model.entity

import com.fasterxml.jackson.annotation.JsonIgnore

import javax.persistence.*

@Entity
@Table(name = "currency")
data class Currency(

        @Id
        @Column(name = "code")
        val code: String = "",

        @Column(name = "name")
        val name: String = "",

        @JsonIgnore
        @Column(name = "def")
        val default: Boolean = false,

        @JsonIgnore
        @ManyToOne(optional = false, fetch = FetchType.LAZY)
        @JoinColumn(foreignKey = ForeignKey(name = "FK_currency_language"))
        private val language: Language? = null,

        @JsonIgnore
        @ManyToMany(fetch = FetchType.LAZY)
        @JoinTable(name = "currency_country",
                joinColumns = [JoinColumn(name = "currency_code")],
                inverseJoinColumns = [JoinColumn(name = "country_code")],
                foreignKey = ForeignKey(name = "FK_currency_code"),
                inverseForeignKey = ForeignKey(name = "FK_country_code"))
        private val countries: Set<Country>? = null

)
