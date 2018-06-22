package com.procurement.mdm.model.entity

import com.fasterxml.jackson.annotation.JsonProperty
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "unit_class")
data class UnitClass(

        @Id
        @Column(name = "code")
        val code: String = "",

        @Column(name = "name")
        val name: String = "",

        @Column(name = "description")
        val description: String = ""
)

data class UnitClassDto(

        @JsonProperty("code")
        val code: String?,

        @JsonProperty("name")
        val name: String?
)

fun List<UnitClass>.getItems(): List<UnitClassDto> =
        this.asSequence().map { UnitClassDto(code = it.code, name = it.name) }.toList()