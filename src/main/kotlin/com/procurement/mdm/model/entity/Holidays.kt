package com.procurement.mdm.model.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import java.util.*

import javax.persistence.*

@Entity
@Table(name = "holidays")
data class Holidays(

        @Id
        @Column(name = "id")
        val id: Long = 0,

        @Column(name = "date")
        val date: Date = Date(),

        @Column(name = "name")
        val name: String = "",

        @JsonIgnore
        @Column(name = "description")
        val description: String = "",

        @JsonIgnore
        @ManyToOne(optional = false, fetch = FetchType.LAZY)
        @JoinColumn(foreignKey = ForeignKey(name = "FK_holidays_language"))
        private val language: Language? = null,

        @JsonIgnore
        @ManyToOne(optional = false, fetch = FetchType.LAZY)
        @JoinColumn(foreignKey = ForeignKey(name = "FK_holidays_country"))
        private val country: Country? = null
)
