package com.procurement.mdm.model.entity

import javax.persistence.*

@Entity
@Table(name = "unit_class")
data class UnitClass (

    @Id
    @Column(name = "code")
    val code: String = "",

    @Column(name = "name")
    val name: String = "",

    @Column(name = "description")
    val description: String = ""
)
