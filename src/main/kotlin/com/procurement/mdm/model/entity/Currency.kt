package com.procurement.mdm.model.entity

import com.fasterxml.jackson.annotation.JsonIgnore

import javax.persistence.*

@Entity
@Table(name = "currency")
data class Currency(
        @JsonIgnore
        @Id
        @Column(name = "id")
        val id: String = "",

        @Column(name = "code")
        val code: String = "",

        @Column(name = "name")
        val name: String = "",

        @JsonIgnore
        @Column(name = "def")
        val default: Boolean = false,

        @JsonIgnore
        @Column(name = "description")
        val description: String = "",

        @JsonIgnore
        @ManyToOne(optional = false, fetch = FetchType.LAZY)
        @JoinColumn(foreignKey = ForeignKey(name = "FK_currency_language"))
        private val language: Language? = null,

        @JsonIgnore
        @ManyToMany(fetch = FetchType.LAZY)
        @JoinTable(name = "currency_country",
                joinColumns = [JoinColumn(name = "currency_id")],
                inverseJoinColumns = [JoinColumn(name = "country_id")],
                foreignKey = ForeignKey(name = "FK_currency"),
                inverseForeignKey = ForeignKey(name = "FK_country"))
        private val countries: Set<Country>? = null
)
