package com.procurement.mdm.domain.model.identifier

data class CriterionIdentifier(
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
