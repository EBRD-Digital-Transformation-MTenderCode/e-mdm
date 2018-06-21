package com.procurement.mdm.model.dto

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ResponseErrorDto(

        @JsonProperty("code")
        var code: String,

        @JsonProperty("description")
        val description: String
)
