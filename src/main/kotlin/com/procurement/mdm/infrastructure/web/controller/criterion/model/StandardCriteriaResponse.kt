package com.procurement.mdm.infrastructure.web.controller.criterion.model

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.procurement.mdm.domain.model.criterion.model.StandardCriteriaResult

class StandardCriteriaResponse(values: List<Criterion>) : List<StandardCriteriaResponse.Criterion> by values {
    data class Criterion(
        @field:JsonProperty("id") @param:JsonProperty("id") val id: String,
        @field:JsonProperty("title") @param:JsonProperty("title") val title: String,

        @JsonInclude(JsonInclude.Include.NON_NULL)
        @field:JsonProperty("description") @param:JsonProperty("description") val description: String?,

        @field:JsonProperty("classification") @param:JsonProperty("classification") val classification: Classification,
        @field:JsonProperty("requirementGroups") @param:JsonProperty("requirementGroups") val requirementGroups: List<RequirementGroup>
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

    companion object {

        fun fromResult(result: StandardCriteriaResult.Criterion): Criterion =
            Criterion(
                id = result.id,
                title = result.title,
                description = result.description,
                classification = result.classification
                    .let { Criterion.Classification(id = it.id, scheme = it.scheme) },
                requirementGroups = result.requirementGroups
                    .map { fromResult(it) }
            )

        private fun fromResult(entity: StandardCriteriaResult.Criterion.RequirementGroup): Criterion.RequirementGroup =
            Criterion.RequirementGroup(
                id = entity.id,
                description = entity.description,
                requirements = entity.requirements.map { this.fromResult(it) }
            )

        private fun fromResult(result: StandardCriteriaResult.Criterion.RequirementGroup.Requirement): Criterion.RequirementGroup.Requirement =
            Criterion.RequirementGroup.Requirement(
                id = result.id,
                description = result.description,
                title = result.title,
                dataType = result.dataType
            )

    }
}