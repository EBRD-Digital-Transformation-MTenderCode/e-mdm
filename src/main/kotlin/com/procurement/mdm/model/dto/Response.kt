package com.procurement.mdm.model.dto

import com.fasterxml.jackson.annotation.JsonInclude
import com.procurement.mdm.exception.ErrorException
import java.util.*

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ResponseDto(

        val errors: List<ResponseErrorDto>?,

        val data: Any?,

        val id: UUID?
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ResponseDataDto(

        val default: Any?,

        val items: Any?
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ResponseErrorDto(

        val code: String,

        val description: String?
)


fun getResponseDto(default: Any? = null, items: Any?, id: UUID? = null): ResponseDto {
    return ResponseDto(
            errors = null,
            data = ResponseDataDto(
                    default = default,
                    items = items),
            id = id)
}

fun getResponseDto(data: Any?, id: UUID? = null): ResponseDto {
    return ResponseDto(
            errors = null,
            data = data,
            id = id)
}

fun getExceptionResponseDto(exception: Exception): ResponseDto {
    return ResponseDto(
            errors = listOf(ResponseErrorDto(
                    code = "400.20.exception",
                    description = exception.message
            )),
            data = null,
            id = null)
}

fun getErrorResponseDto(error: ErrorException, id: UUID? = null): ResponseDto {
    return ResponseDto(
            errors = listOf(ResponseErrorDto(
                    code = "400.20." + error.code,
                    description = error.msg
            )),
            data = null,
            id = id)
}