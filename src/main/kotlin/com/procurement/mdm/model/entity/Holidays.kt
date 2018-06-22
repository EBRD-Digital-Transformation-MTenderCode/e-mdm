package com.procurement.mdm.model.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import java.sql.Date

import javax.persistence.*

@Entity
@Table(name = "holidays")
data class Holidays(
        @JsonIgnore
        @Id
        @Column(name = "id")
        val id: String = "",

        @Column(name = "code")
        val code: String = "",

        @Column(name = "holiday_date")
        val date: Date = Date(0),

        @Column(name = "name")
        val name: String = "",

        @JsonIgnore
        @Column(name = "description")
        val description: String = "",

        @JsonIgnore
        @ManyToOne(optional = false, fetch = FetchType.LAZY)
        @JoinColumn(foreignKey = ForeignKey(name = "FK_holidays_country"))
        private val country: Country? = null
)
