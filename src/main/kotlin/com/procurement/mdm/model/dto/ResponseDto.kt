package com.procurement.budget.model.bpe

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ResponseDto(

        @JsonProperty("default")
        val default: Any?,

        @JsonProperty("data")
        val data: Any?
)
