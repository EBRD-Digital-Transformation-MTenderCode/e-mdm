package com.procurement.mdm.domain.model.criterion.model

import com.procurement.mdm.model.entity.StandardCriterionEntity

class StandardCriteriaResult(values: List<Criterion>) : List<StandardCriteriaResult.Criterion> by values {

    data class Criterion(
        val id: String,
        val title: String,
        val source: String,
        val relatesTo: String,
        val classification: Classification,
        val requirementGroups: List<RequirementGroup>,
        val description: String?
    ) {
        data class Classification(
            val id: String,
            val scheme: String
        )

        data class RequirementGroup(
            val id: String,
            val description: String?,
            val requirements: List<Requirement>
        ) {
            class Requirement(
                val id: String,
                val title: String,
                val description: String?,
                val dataType: String,
                val expectedValue: Any?
            )
        }
    }

    companion object {

        fun fromEntity(entity: StandardCriterionEntity): Criterion =
            Criterion(
                id = entity.id,
                title = entity.criterion.title,
                source = entity.criterion.source,
                relatesTo = entity.criterion.relatesTo,
                description = entity.criterion.description,
                classification = entity.criterion.classification
                    .let { Criterion.Classification(id = it.id, scheme = it.scheme) },
                requirementGroups = entity.criterion.requirementGroups
                    .map { fromEntity(it) }
            )

        private fun fromEntity(entity: StandardCriterionEntity.StandardCriterion.RequirementGroup): Criterion.RequirementGroup =
            Criterion.RequirementGroup(
                id = entity.id,
                description = entity.description,
                requirements = entity.requirements.map { fromEntity(it) }
            )

        private fun fromEntity(entity: StandardCriterionEntity.StandardCriterion.RequirementGroup.Requirement): Criterion.RequirementGroup.Requirement =
            Criterion.RequirementGroup.Requirement(
                id = entity.id,
                description = entity.description,
                title = entity.title,
                dataType = entity.dataType,
                expectedValue = entity.expectedValue
            )
    }
}
