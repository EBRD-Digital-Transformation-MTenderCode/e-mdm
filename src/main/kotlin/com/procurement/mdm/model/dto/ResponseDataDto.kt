package com.procurement.mdm.model.dto

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ResponseDataDto(

        @JsonProperty("default")
        var default: Any?,

        @JsonProperty("items")
        val items: Any?
)
