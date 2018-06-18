package com.procurement.mdm.model.entity

import javax.persistence.*

@Entity
@Table(name = "languages")
data class Language (

    @Id
    @Column(name = "code")
    private val code: String,

    @Column(name = "name")
    private val name: String,

    @Column(name = "description")
    private val description: String
)
