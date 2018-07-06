package com.procurement.mdm.model.dto

import com.fasterxml.jackson.annotation.JsonInclude
import com.procurement.mdm.exception.ErrorException

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ResponseDto(

        val errors: List<ResponseErrorDto>?,

        val data: Any?
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ResponseDataDto(

        var default: Any?,

        val items: Any?
)

data class ResponseErrorDto(

        var code: String,

        val description: String
)


fun getResponseDto(default: Any?, items: Any?): ResponseDto {
    return ResponseDto(
            errors = null,
            data = ResponseDataDto(
                    default = default,
                    items = items))
}

fun getErrorResponseDto(error: ErrorException): ResponseDto {
    return ResponseDto(
            errors = listOf(ResponseErrorDto(
                    code = "400.20." + error.code,
                    description = error.msg
            )),
            data = null)
}