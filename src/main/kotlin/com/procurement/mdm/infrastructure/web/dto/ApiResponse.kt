package com.procurement.mdm.infrastructure.web.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder

@JsonPropertyOrder("data")
abstract class ApiResponse<T>(
    @field:JsonProperty("data") @param:JsonProperty("data") val data: T
)
