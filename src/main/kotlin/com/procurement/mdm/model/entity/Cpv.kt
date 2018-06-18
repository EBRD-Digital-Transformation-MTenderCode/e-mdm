package com.procurement.mdm.model.entity

import com.fasterxml.jackson.annotation.JsonIgnore

import javax.persistence.*


@Entity
@Table(name = "cpv")
data class Cpv (

    @Id
    @Column(name = "code")
    private val code: String,

    @Column(name = "name")
    private val name: String,

    @Column(name = "group")
    private val group: Int,

    @Column(name = "parent")
    private val parent: String,

    @JsonIgnore
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = ForeignKey(name = "FK_cpv_language"))
    private val language: Language
)
