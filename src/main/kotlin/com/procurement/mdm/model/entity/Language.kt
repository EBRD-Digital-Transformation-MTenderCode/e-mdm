package com.procurement.mdm.model.entity

import javax.persistence.*

@Entity
@Table(name = "languages")
data class Language (

    @Id
    @Column(name = "iso6391")
    private val iso6391: String,

    @Column(name = "name")
    private val name: String,

    @Column(name = "family")
    private val family: String,

    @Column(name = "description")
    private val description: String
)
