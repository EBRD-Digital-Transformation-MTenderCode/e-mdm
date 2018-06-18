package com.procurement.mdm.model.entity

import com.fasterxml.jackson.annotation.JsonIgnore

import javax.persistence.*

@Entity
@Table(name = "countries")
data class Country (

    @Id
    @Column(name = "code")
    private val code: String,

    @Column(name = "name")
    private val name: String,

    @Column(name = "description")
    private val description: String,

    @JsonIgnore
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = ForeignKey(name = "FK_countries_language"))
    private val language: Language

)
