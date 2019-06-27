package com.procurement.mdm.infrastructure.web.dto

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import org.springframework.http.HttpStatus

@JsonPropertyOrder("errors")
data class ApiError(
    @JsonIgnore val status: HttpStatus,
    @field:JsonProperty("errors") @param:JsonProperty("errors") val errors: List<Error>
) {
    @JsonPropertyOrder("code", "description")
    data class Error(
        @field:JsonProperty("code") @param:JsonProperty("code") val code: ErrorCode,
        @field:JsonProperty("description") @param:JsonProperty("description") val description: String
    )
}
