package com.procurement.mdm.infrastructure.web.dto

import com.fasterxml.jackson.annotation.JsonValue
import com.procurement.mdm.application.GlobalProperties
import org.springframework.http.HttpStatus

enum class ErrorCode(val status: HttpStatus, group: GroupError, id: String) {

    INTERNAL_SERVER_ERROR(status = HttpStatus.INTERNAL_SERVER_ERROR, group = Groups.SERVER, id = "00"),

    /**
     * Request parameter is missing.
     */
    LANGUAGE_REQUEST_PARAMETER_MISSING(status = HttpStatus.BAD_REQUEST, group = Groups.REQUEST_PARAMETER_MISSING, id = "01"),

    /**
     * Request parameter is unknown.
     */
    LANGUAGE_REQUEST_PARAMETER_UNKNOWN(status = HttpStatus.BAD_REQUEST, group = Groups.REQUEST_PARAMETER_UNKNOWN, id = "01"),

    /**
     * Language.
     */
    INVALID_LANGUAGE_CODE(status = HttpStatus.BAD_REQUEST, group = Groups.LANGUAGE, id = "01"),

    /**
     * Country.
     */
    INVALID_COUNTRY_CODE(status = HttpStatus.BAD_REQUEST, group = Groups.COUNTRY, id = "01"),
    COUNTRY_NOT_FOUND(status = HttpStatus.NOT_FOUND, group = Groups.COUNTRY, id = "02");

    @JsonValue
    val code: String = "${status.value()}.${GlobalProperties.serviceId}.$group.$id"

    enum class Groups(override val code: String) : GroupError {
        SERVER(code = "00"),
        REQUEST_PARAMETER_MISSING(code = "01"),
        REQUEST_PARAMETER_UNKNOWN(code = "02"),
        LANGUAGE(code = "10"),
        COUNTRY(code = "11");

        override fun toString(): String = code
    }
}

interface GroupError {
    val code: String
}
