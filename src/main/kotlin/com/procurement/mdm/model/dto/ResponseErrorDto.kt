package com.procurement.mdm.model.dto

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

data class ResponseErrorDto(

        var code: String,

        val description: String
)
