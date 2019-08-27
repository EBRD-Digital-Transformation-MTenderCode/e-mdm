package com.procurement.mdm.domain.model.identifier

data class RegionIdentifier(
    val scheme: String,
    val id: String,
    val description: String,
    val uri: String
)
