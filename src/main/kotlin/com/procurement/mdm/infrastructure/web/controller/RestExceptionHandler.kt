package com.procurement.mdm.infrastructure.web.controller

import com.procurement.mdm.application.exception.ApplicationException
import com.procurement.mdm.application.exception.CountryNotFoundException
import com.procurement.mdm.domain.exception.DomainException
import com.procurement.mdm.domain.exception.InvalidCountryCodeException
import com.procurement.mdm.domain.exception.InvalidLanguageCodeException
import com.procurement.mdm.domain.exception.LanguageUnknownException
import com.procurement.mdm.infrastructure.exception.InfrastructureException
import com.procurement.mdm.infrastructure.exception.LanguageRequestParameterMissingException
import com.procurement.mdm.infrastructure.web.dto.ApiError
import com.procurement.mdm.infrastructure.web.dto.ErrorCode
import com.procurement.mdm.infrastructure.web.dto.ErrorCode.COUNTRY_NOT_FOUND
import com.procurement.mdm.infrastructure.web.dto.ErrorCode.INTERNAL_SERVER_ERROR
import com.procurement.mdm.infrastructure.web.dto.ErrorCode.INVALID_COUNTRY_CODE
import com.procurement.mdm.infrastructure.web.dto.ErrorCode.INVALID_LANGUAGE_CODE
import com.procurement.mdm.infrastructure.web.dto.ErrorCode.LANGUAGE_REQUEST_PARAMETER_MISSING
import com.procurement.mdm.infrastructure.web.dto.ErrorCode.LANGUAGE_REQUEST_PARAMETER_UNKNOWN
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

            is LanguageUnknownException ->
                exception.handler(errorCode = LANGUAGE_REQUEST_PARAMETER_UNKNOWN)
        }

        return ResponseEntity.status(apiError.status).body(apiError)
    }

    @ExceptionHandler(value = [ApplicationException::class])
    fun applicationExceptionHandler(exception: ApplicationException): ResponseEntity<ApiError> {
        val apiError: ApiError = when (exception) {
            is CountryNotFoundException ->
                exception.handler(errorCode = COUNTRY_NOT_FOUND)
        }

        return ResponseEntity.status(apiError.status).body(apiError)
    }

    @ExceptionHandler(value = [InfrastructureException::class])
    fun infrastructureExceptionHandler(exception: InfrastructureException): ResponseEntity<ApiError> {
        val apiError: ApiError = when (exception) {
            is LanguageRequestParameterMissingException ->
                exception.handler(errorCode = LANGUAGE_REQUEST_PARAMETER_MISSING)
        }

        return ResponseEntity.status(apiError.status).body(apiError)
    }

    @ExceptionHandler(value = [Exception::class])
    fun exceptionHandler(exception: Exception): ResponseEntity<ApiError> {
        log.error("Internal server error.", this)
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
