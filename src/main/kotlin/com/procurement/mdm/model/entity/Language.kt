package com.procurement.mdm.model.entity

import com.fasterxml.jackson.annotation.JsonProperty
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "language")
data class Language(

        @Id
        @Column(name = "code", length = 2)
        val code: String = "",

        @Column(name = "name")
        val name: String = "",

        @Column(name = "description")
        val description: String = ""
)

data class LanguageDto(

        @JsonProperty("code")
        val code: String?,

        @JsonProperty("name")
        val name: String?
)

fun List<Language>.getItems(): List<LanguageDto> =
        this.asSequence().map { LanguageDto(code = it.code, name = it.name) }.toList()
