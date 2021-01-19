package com.procurement.mdm.domain.entity

data class CriterionEntity(
    val id: String,
    val title: String,
    val description: String?,
    val classification: Classification
) {
    data class Classification(
        val id: String,
        val scheme: String
    )
}