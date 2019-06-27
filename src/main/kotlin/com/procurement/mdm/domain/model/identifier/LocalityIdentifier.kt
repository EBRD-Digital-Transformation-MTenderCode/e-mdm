package com.procurement.mdm.domain.model.identifier

data class LocalityIdentifier(
    val scheme: String,
    val id: String,
    val description: String,
    val uri: String
)
