package com.procurement.mdm.model.dto

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.procurement.mdm.exception.ExternalErrorException
import com.procurement.mdm.exception.InternalErrorException

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ResponseDto(

        @get:JsonProperty("success")
        val success: Boolean?,

        @JsonProperty("details")
        @JsonInclude(JsonInclude.Include.NON_NULL)
        val details: List<ResponseDetailsDto>?,

        @JsonProperty("errors")
        val errors: List<ResponseErrorDto>?,

        @JsonProperty("data")
        val data: ResponseDataDto?
)

fun getResponseDto(default: Any?, items: Any?, internal: Boolean): ResponseDto {
    return when (!internal) {
        true -> ResponseDto(
                success = null,
                details = null,
                errors = null,
                data = ResponseDataDto(
                        default = default,
                        items = items))
        false -> ResponseDto(
                success = true,
                details = null,
                errors = null,
                data = ResponseDataDto(
                        default = default,
                        items = items))
    }
}

fun getExternalErrorResponseDto(error: ExternalErrorException): ResponseDto {
    return ResponseDto(
            success = null,
            details = null,
            errors = listOf(ResponseErrorDto(
                    code = "400.20." + error.code,
                    description = error.msg
            )),
            data = null)
}

fun getInternalErrorResponseDto(error: InternalErrorException): ResponseDto {
    return ResponseDto(
            success = false,
            details = listOf(ResponseDetailsDto(
                    code = "400.20." + error.code,
                    message = error.msg
            )),
            errors = null,
            data = null)
}