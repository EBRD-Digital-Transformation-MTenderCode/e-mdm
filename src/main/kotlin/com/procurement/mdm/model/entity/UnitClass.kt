package com.procurement.mdm.model.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "unit_class")
data class UnitClass(
        @JsonIgnore
        @Id
        @Column(name = "id")
        val id: String = "",

        @Column(name = "code")
        val code: String = "",

        @Column(name = "name")
        val name: String = "",

        @Column(name = "description")
        val description: String = ""
)
