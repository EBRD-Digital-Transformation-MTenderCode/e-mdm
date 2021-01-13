package com.procurement.mdm.model.entity

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.procurement.mdm.domain.model.ProcurementCategory

data class StandardCriterionEntity(
    val id: String,
    val country: String,
    val language: String,
    val criterion: StandardCriterion
) {
    data class StandardCriterion(
        @field:JsonProperty("id") @param:JsonProperty("id") val id: String,
        @field:JsonProperty("title") @param:JsonProperty("title") val title: String,
        @field:JsonProperty("classification") @param:JsonProperty("classification") val classification: Classification,
        @field:JsonProperty("requirementGroups") @param:JsonProperty("requirementGroups") val requirementGroups: List<RequirementGroup>,

        @JsonInclude(JsonInclude.Include.NON_NULL)
        @field:JsonProperty("description") @param:JsonProperty("description") val description: String?,

        @field:JsonProperty("mainProcurementCategories") @param:JsonProperty("mainProcurementCategories") val mainProcurementCategories: List<ProcurementCategory>?
    ) {
        data class Classification(
            @field:JsonProperty("id") @param:JsonProperty("id") val id: String,
            @field:JsonProperty("scheme") @param:JsonProperty("scheme") val scheme: String
        )

        data class RequirementGroup(
            @field:JsonProperty("id") @param:JsonProperty("id") val id: String,

            @JsonInclude(JsonInclude.Include.NON_NULL)
            @field:JsonProperty("description") @param:JsonProperty("description") val description: String?,

            @field:JsonProperty("requirements") @param:JsonProperty("requirements") val requirements: List<Requirement>
        ) {
            class Requirement(
                @field:JsonProperty("id") @param:JsonProperty("id") val id: String,
                @field:JsonProperty("title") @param:JsonProperty("title") val title: String,
                @field:JsonProperty("description") @param:JsonProperty("description") val description: String?,
                @field:JsonProperty("dataType") @param:JsonProperty("dataType") val dataType: String
            )
        }

    }
}