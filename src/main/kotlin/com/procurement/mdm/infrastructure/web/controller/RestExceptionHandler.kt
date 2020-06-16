package com.procurement.mdm.infrastructure.web.controller

import com.procurement.mdm.application.exception.ApplicationException
import com.procurement.mdm.application.exception.CountryDescriptionNotFoundException
import com.procurement.mdm.application.exception.CountryNotFoundException
import com.procurement.mdm.application.exception.CountrySchemeNotFoundException
import com.procurement.mdm.application.exception.LocalityNotFoundException
import com.procurement.mdm.application.exception.LocalityNotLinkedToRegionException
import com.procurement.mdm.application.exception.LocalitySchemeNotFoundException
import com.procurement.mdm.application.exception.OrganizationScaleNotFoundException
import com.procurement.mdm.application.exception.OrganizationSchemeNotFoundException
import com.procurement.mdm.application.exception.RegionNotFoundException
import com.procurement.mdm.application.exception.RegionNotLinkedToCountryException
import com.procurement.mdm.application.exception.RegionSchemeNotFoundException
import com.procurement.mdm.domain.exception.CountryUnknownException
import com.procurement.mdm.domain.exception.DomainException
import com.procurement.mdm.domain.exception.InvalidCountryCodeException
import com.procurement.mdm.domain.exception.InvalidCountrySchemeException
import com.procurement.mdm.domain.exception.InvalidLanguageCodeException
import com.procurement.mdm.domain.exception.InvalidLocalityCodeException
import com.procurement.mdm.domain.exception.InvalidLocalitySchemeException
import com.procurement.mdm.domain.exception.InvalidRegionCodeException
import com.procurement.mdm.domain.exception.InvalidRegionSchemeException
import com.procurement.mdm.domain.exception.LanguageUnknownException
import com.procurement.mdm.infrastructure.exception.CountryRequestParameterMissingException
import com.procurement.mdm.infrastructure.exception.InfrastructureException
import com.procurement.mdm.infrastructure.exception.LanguageRequestParameterMissingException
import com.procurement.mdm.infrastructure.exception.NoHandlerUrlException
import com.procurement.mdm.infrastructure.exception.RequestPayloadMissingException
import com.procurement.mdm.infrastructure.web.dto.ApiError
import com.procurement.mdm.infrastructure.web.dto.ErrorCode
import com.procurement.mdm.infrastructure.web.dto.ErrorCode.COUNTRY_DESCRIPTION_NOT_FOUND
import com.procurement.mdm.infrastructure.web.dto.ErrorCode.COUNTRY_NOT_FOUND
import com.procurement.mdm.infrastructure.web.dto.ErrorCode.COUNTRY_REQUEST_PARAMETER_MISSING
import com.procurement.mdm.infrastructure.web.dto.ErrorCode.COUNTRY_REQUEST_PARAMETER_UNKNOWN
import com.procurement.mdm.infrastructure.web.dto.ErrorCode.COUNTRY_SCHEME_NOT_FOUND
import com.procurement.mdm.infrastructure.web.dto.ErrorCode.INTERNAL_SERVER_ERROR
import com.procurement.mdm.infrastructure.web.dto.ErrorCode.INVALID_COUNTRY_CODE
import com.procurement.mdm.infrastructure.web.dto.ErrorCode.INVALID_COUNTRY_SCHEME
import com.procurement.mdm.infrastructure.web.dto.ErrorCode.INVALID_LANGUAGE_CODE
import com.procurement.mdm.infrastructure.web.dto.ErrorCode.INVALID_LOCALITY_CODE
import com.procurement.mdm.infrastructure.web.dto.ErrorCode.INVALID_LOCALITY_SCHEME
import com.procurement.mdm.infrastructure.web.dto.ErrorCode.INVALID_REGION_CODE
import com.procurement.mdm.infrastructure.web.dto.ErrorCode.INVALID_REGION_SCHEME
import com.procurement.mdm.infrastructure.web.dto.ErrorCode.INVALID_URL
import com.procurement.mdm.infrastructure.web.dto.ErrorCode.LANGUAGE_REQUEST_PARAMETER_MISSING
import com.procurement.mdm.infrastructure.web.dto.ErrorCode.LANGUAGE_REQUEST_PARAMETER_UNKNOWN
import com.procurement.mdm.infrastructure.web.dto.ErrorCode.LOCALITY_NOT_FOUND
import com.procurement.mdm.infrastructure.web.dto.ErrorCode.LOCALITY_NOT_LINKED_TO_REGION
import com.procurement.mdm.infrastructure.web.dto.ErrorCode.LOCALITY_SCHEME_NOT_FOUND
import com.procurement.mdm.infrastructure.web.dto.ErrorCode.ORGANIZATION_SCALE_NOT_FOUND
import com.procurement.mdm.infrastructure.web.dto.ErrorCode.ORGANIZATION_SCHEME_NOT_FOUND
import com.procurement.mdm.infrastructure.web.dto.ErrorCode.REGION_NOT_FOUND
import com.procurement.mdm.infrastructure.web.dto.ErrorCode.REGION_NOT_LINKED_TO_COUNTRY
import com.procurement.mdm.infrastructure.web.dto.ErrorCode.REGION_SCHEME_NOT_FOUND
import com.procurement.mdm.infrastructure.web.dto.ErrorCode.REQUEST_PAYLOAD_MISSING
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@ControllerAdvice
class RestExceptionHandler : ResponseEntityExceptionHandler() {
    companion object {
        private val log: Logger = LoggerFactory.getLogger(RestExceptionHandler::class.java)
    }

    @ExceptionHandler(value = [DomainException::class])
    fun domainExceptionHandler(exception: DomainException): ResponseEntity<ApiError> {
        val apiError: ApiError = when (exception) {
            is InvalidLanguageCodeException ->
                exception.handler(errorCode = INVALID_LANGUAGE_CODE)

            is InvalidCountryCodeException ->
                exception.handler(errorCode = INVALID_COUNTRY_CODE)

            is InvalidRegionCodeException ->
                exception.handler(errorCode = INVALID_REGION_CODE)

            is InvalidLocalityCodeException ->
                exception.handler(errorCode = INVALID_LOCALITY_CODE)

            is LanguageUnknownException ->
                exception.handler(errorCode = LANGUAGE_REQUEST_PARAMETER_UNKNOWN)

            is CountryUnknownException ->
                exception.handler(errorCode = COUNTRY_REQUEST_PARAMETER_UNKNOWN)

            is InvalidCountrySchemeException ->
                exception.handler(errorCode = INVALID_COUNTRY_SCHEME)

            is InvalidRegionSchemeException ->
                exception.handler(errorCode = INVALID_REGION_SCHEME)

            is InvalidLocalitySchemeException ->
                exception.handler(errorCode = INVALID_LOCALITY_SCHEME)
        }

        return ResponseEntity.status(apiError.status).body(apiError)
    }

    @ExceptionHandler(value = [ApplicationException::class])
    fun applicationExceptionHandler(exception: ApplicationException): ResponseEntity<ApiError> {
        val apiError: ApiError = when (exception) {
            is CountryNotFoundException ->
                exception.handler(errorCode = COUNTRY_NOT_FOUND)

            is RegionNotFoundException ->
                exception.handler(errorCode = REGION_NOT_FOUND)

            is LocalityNotFoundException ->
                exception.handler(errorCode = LOCALITY_NOT_FOUND)

            is OrganizationSchemeNotFoundException ->
                exception.handler(errorCode = ORGANIZATION_SCHEME_NOT_FOUND)

            is OrganizationScaleNotFoundException ->
                exception.handler(errorCode = ORGANIZATION_SCALE_NOT_FOUND)

            is CountrySchemeNotFoundException ->
                exception.handler(errorCode = COUNTRY_SCHEME_NOT_FOUND)

            is RegionSchemeNotFoundException ->
                exception.handler(errorCode = REGION_SCHEME_NOT_FOUND)

            is LocalitySchemeNotFoundException ->
                exception.handler(errorCode = LOCALITY_SCHEME_NOT_FOUND)

            is RegionNotLinkedToCountryException ->
                exception.handler(errorCode = REGION_NOT_LINKED_TO_COUNTRY)

            is LocalityNotLinkedToRegionException ->
                exception.handler(errorCode = LOCALITY_NOT_LINKED_TO_REGION)

            is CountryDescriptionNotFoundException ->
                exception.handler(errorCode = COUNTRY_DESCRIPTION_NOT_FOUND)
        }

        return ResponseEntity.status(apiError.status).body(apiError)
    }

    @ExceptionHandler(value = [InfrastructureException::class])
    fun infrastructureExceptionHandler(exception: InfrastructureException): ResponseEntity<ApiError> {
        val apiError: ApiError = when (exception) {
            is NoHandlerUrlException ->
                exception.handler(errorCode = INVALID_URL)

            is LanguageRequestParameterMissingException ->
                exception.handler(errorCode = LANGUAGE_REQUEST_PARAMETER_MISSING)

            is CountryRequestParameterMissingException ->
                exception.handler(errorCode = COUNTRY_REQUEST_PARAMETER_MISSING)

            is RequestPayloadMissingException ->
                exception.handler(errorCode = REQUEST_PAYLOAD_MISSING)
        }

        return ResponseEntity.status(apiError.status).body(apiError)
    }

    @ExceptionHandler(value = [Exception::class])
    fun exceptionHandler(exception: Exception): ResponseEntity<ApiError> {
        log.error("Internal server error.", exception)
        val apiError: ApiError = apiError(errorCode = INTERNAL_SERVER_ERROR, description = "Internal server error.")
        return ResponseEntity.status(apiError.status).body(apiError)
    }

    private fun DomainException.handler(errorCode: ErrorCode): ApiError {
        log.warn(description)
        return apiError(errorCode = errorCode, description = this.description)
    }

    private fun ApplicationException.handler(errorCode: ErrorCode): ApiError {
        log.warn(description)
        return apiError(errorCode = errorCode, description = this.description)
    }

    private fun InfrastructureException.handler(errorCode: ErrorCode): ApiError {
        log.warn(this.description)
        return apiError(errorCode = errorCode, description = this.description)
    }

    private fun apiError(errorCode: ErrorCode, description: String) =
        ApiError(
            status = errorCode.status,
            errors = listOf(
                ApiError.Error(
                    code = errorCode,
                    description = description
                )
            )
        )
}
