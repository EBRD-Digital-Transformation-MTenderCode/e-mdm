package com.procurement.mdm.domain.model.identifier

data class CountryIdentifier(
    val scheme: String,
    val id: String,
    val description: String,
    val uri: String
)
