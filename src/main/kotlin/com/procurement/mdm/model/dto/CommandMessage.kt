package com.procurement.mdm.model.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonValue
import com.fasterxml.jackson.databind.JsonNode
import com.procurement.mdm.exception.ExErrorException
import com.procurement.mdm.exception.InErrorException

data class CommandMessage @JsonCreator constructor(

        val id: String,

        val command: CommandType,

        val context: Context,

        val data: JsonNode,

        val version: ApiVersion
)

data class Context @JsonCreator constructor(
        val country: String,
        val language: String,
        val pmd: String?
)

enum class CommandType(private val value: String) {
    PROCESS_BID_DATA("processBidData"),
    PROCESS_CONTRACT_DATA("processContractData"),
    PROCESS_EI_DATA("processEiData"),
    PROCESS_ENQUIRY_DATA("processEnquiryData"),
    PROCESS_FS_DATA("processFsData"),
    PROCESS_TENDER_DATA("processTenderData"),
    VALIDATE_AP("validateAP"),
    ENRICH_DATA_FOR_UPDATE_AP("enrichDataForUpdateAP");

    @JsonValue
    fun value(): String {
        return this.value
    }

    override fun toString(): String {
        return this.value
    }
}

enum class ApiVersion(private val value: String) {
    V_0_0_1("0.0.1");

    @JsonValue
    fun value(): String {
        return this.value
    }

    override fun toString(): String {
        return this.value
    }
}


@JsonInclude(JsonInclude.Include.NON_NULL)
data class ResponseDto(

        val errors: List<ResponseErrorDto>?,

        val data: Any?,

        val id: String?
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

fun getResponseDto(default: Any? = null, items: Any?, id: String? = null): ResponseDto {
    return ResponseDto(
            errors = null,
            data = ResponseDataDto(
                    default = default,
                    items = items),
            id = id)
}

fun getResponseDto(data: Any?, id: String? = null): ResponseDto {
    return ResponseDto(
            errors = null,
            data = data,
            id = id)
}

fun getExceptionResponseDto(exception: Exception): ResponseDto {
    return ResponseDto(
            errors = listOf(ResponseErrorDto(
                    code = "400.20.00",
                    description = exception.message
            )),
            data = null,
            id = null)
}

fun getInErrorResponseDto(error: InErrorException): ResponseDto {
    return ResponseDto(
            errors = listOf(ResponseErrorDto(
                    code = "400.20." + error.code,
                    description = error.msg
            )),
            data = null,
            id = error.id)
}

fun getExErrorResponseDto(error: ExErrorException): ResponseDto {
    return ResponseDto(
            errors = listOf(ResponseErrorDto(
                    code = "400.20." + error.code,
                    description = error.msg
            )),
            data = null,
            id = null)
}
