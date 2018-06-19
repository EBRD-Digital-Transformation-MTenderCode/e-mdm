package com.procurement.mdm.model.entity

import javax.persistence.*

@Entity
@Table(name = "languages")
data class Language (

    @Id
    @Column(name = "code")
    val code: String = "",

    @Column(name = "name")
    val name: String = "",

    @Column(name = "description")
    val description: String = ""
)
