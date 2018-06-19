package com.procurement.mdm.model.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import java.util.*

import javax.persistence.*

@Entity
@Table(name = "gpa_annexes")
data class GPAannexes(

        @Id
        @Column(name = "code")
        val code: String = "",

        @Column(name = "name")
        val name: String = "",

        @JsonIgnore
        @Column(name = "description")
        val description: String = "",

        @JsonIgnore
        @ManyToOne(optional = false, fetch = FetchType.LAZY)
        @JoinColumn(foreignKey = ForeignKey(name = "FK_bank_language"))
        private val language: Language? = null,

        @JsonIgnore
        @ManyToOne(optional = false, fetch = FetchType.LAZY)
        @JoinColumn(foreignKey = ForeignKey(name = "FK_bank_country"))
        private val country: Country? = null
)
