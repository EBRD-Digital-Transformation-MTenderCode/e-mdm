package com.procurement.mdm.infrastructure.web.dto

import com.fasterxml.jackson.annotation.JsonValue
import com.procurement.mdm.application.GlobalProperties
import org.springframework.http.HttpStatus

enum class ErrorCode(val status: HttpStatus, group: GroupError, id: String) {

    INTERNAL_SERVER_ERROR(status = HttpStatus.INTERNAL_SERVER_ERROR, group = Groups.SERVER, id = "00"),
    INVALID_URL(status = HttpStatus.NOT_FOUND, group = Groups.SERVER, id = "01"),

    /**
     * Request parameter is missing.
     */
    LANGUAGE_REQUEST_PARAMETER_MISSING(status = HttpStatus.BAD_REQUEST, group = Groups.REQUEST_PARAMETER_MISSING, id = "01"),
    COUNTRY_REQUEST_PARAMETER_MISSING(status = HttpStatus.BAD_REQUEST, group = Groups.REQUEST_PARAMETER_MISSING, id = "02"),
    PMD_REQUEST_PARAMETER_MISSING(status = HttpStatus.BAD_REQUEST, group = Groups.REQUEST_PARAMETER_MISSING, id = "03"),
    PHASE_REQUEST_PARAMETER_MISSING(status = HttpStatus.BAD_REQUEST, group = Groups.REQUEST_PARAMETER_MISSING, id = "04"),
    CRITERION_REQUEST_PARAMETER_MISSING(status = HttpStatus.BAD_REQUEST, group = Groups.REQUEST_PARAMETER_MISSING, id = "05"),

    /**
     * Request parameter is unknown.
     */
    LANGUAGE_REQUEST_PARAMETER_UNKNOWN(status = HttpStatus.BAD_REQUEST, group = Groups.REQUEST_PARAMETER_UNKNOWN, id = "01"),
    COUNTRY_REQUEST_PARAMETER_UNKNOWN(status = HttpStatus.BAD_REQUEST, group = Groups.REQUEST_PARAMETER_UNKNOWN, id = "02"),

    /**
     * Request payload is missing.
     */
    REQUEST_PAYLOAD_MISSING(status = HttpStatus.BAD_REQUEST, group = Groups.REQUEST_PAYLOAD_UNKNOWN, id = "01"),

    /**
     * Language.
     */
    INVALID_LANGUAGE_CODE(status = HttpStatus.BAD_REQUEST, group = Groups.LANGUAGE, id = "01"),

    /**
     * Country.
     */
    INVALID_COUNTRY_CODE(status = HttpStatus.BAD_REQUEST, group = Groups.COUNTRY, id = "01"),
    COUNTRY_NOT_FOUND(status = HttpStatus.NOT_FOUND, group = Groups.COUNTRY, id = "02"),
    COUNTRY_SCHEME_NOT_FOUND(status = HttpStatus.NOT_FOUND, group = Groups.COUNTRY, id = "03"),
    INVALID_COUNTRY_SCHEME(status = HttpStatus.BAD_REQUEST, group = Groups.COUNTRY, id = "04"),
    COUNTRY_DESCRIPTION_NOT_FOUND(status = HttpStatus.NOT_FOUND, group = Groups.COUNTRY, id = "05"),


    /**
     * Region.
     */
    INVALID_REGION_CODE(status = HttpStatus.BAD_REQUEST, group = Groups.REGION, id = "01"),
    REGION_NOT_FOUND(status = HttpStatus.NOT_FOUND, group = Groups.REGION, id = "02"),
    REGION_SCHEME_NOT_FOUND(status = HttpStatus.NOT_FOUND, group = Groups.REGION, id = "03"),
    INVALID_REGION_SCHEME(status = HttpStatus.BAD_REQUEST, group = Groups.REGION, id = "04"),
    REGION_DESCRIPTION_NOT_FOUND(status = HttpStatus.NOT_FOUND, group = Groups.REGION, id = "05"),
    REGION_NOT_LINKED_TO_COUNTRY(status = HttpStatus.NOT_FOUND, group = Groups.REGION, id = "06"),

    /**
     * Locality.
     */
    INVALID_LOCALITY_CODE(status = HttpStatus.BAD_REQUEST, group = Groups.LOCALITY, id = "01"),
    LOCALITY_NOT_FOUND(status = HttpStatus.NOT_FOUND, group = Groups.LOCALITY, id = "02"),
    LOCALITY_SCHEME_NOT_FOUND(status = HttpStatus.NOT_FOUND, group = Groups.LOCALITY, id = "03"),
    INVALID_LOCALITY_SCHEME(status = HttpStatus.BAD_REQUEST, group = Groups.LOCALITY, id = "04"),
    LOCALITY_DESCRIPTION_NOT_FOUND(status = HttpStatus.NOT_FOUND, group = Groups.LOCALITY, id = "05"),
    LOCALITY_NOT_LINKED_TO_REGION(status = HttpStatus.NOT_FOUND, group = Groups.LOCALITY, id = "06"),

    /**
     * Organization scheme
     */
    ORGANIZATION_SCHEME_NOT_FOUND(status = HttpStatus.NOT_FOUND, group = Groups.ORGANIZATION_SCHEME, id = "01"),

    /**
     * Organization scale
     */
    ORGANIZATION_SCALE_NOT_FOUND(status = HttpStatus.NOT_FOUND, group = Groups.ORGANIZATION_SCALE, id = "01"),

    /**
     * Tender process
     */
    INVALID_PMD(status = HttpStatus.BAD_REQUEST, group = Groups.TENDER_PROCESS, id = "01"),
    INVALID_PHASE(status = HttpStatus.BAD_REQUEST, group = Groups.TENDER_PROCESS, id = "02"),
    INVALID_CRITERION(status = HttpStatus.BAD_REQUEST, group = Groups.TENDER_PROCESS, id = "03");

    @JsonValue
    val code: String = "${status.value()}.${GlobalProperties.serviceId}.$group.$id"

    enum class Groups(override val code: String) : GroupError {
        SERVER(code = "00"),
        REQUEST_PARAMETER_MISSING(code = "01"),
        REQUEST_PARAMETER_UNKNOWN(code = "02"),
        REQUEST_PAYLOAD_UNKNOWN(code = "03"),
        LANGUAGE(code = "10"),
        COUNTRY(code = "11"),
        REGION(code = "12"),
        LOCALITY(code = "13"),
        ORGANIZATION_SCHEME(code = "14"),
        ORGANIZATION_SCALE(code = "15"),
        TENDER_PROCESS(code = "16");

        override fun toString(): String = code
    }
}

interface GroupError {
    val code: String
}
