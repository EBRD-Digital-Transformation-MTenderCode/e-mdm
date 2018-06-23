package com.procurement.mdm.controller

import com.procurement.mdm.exception.ExternalErrorException
import com.procurement.mdm.exception.InternalErrorException
import com.procurement.mdm.model.dto.getExternalErrorResponseDto
import com.procurement.mdm.model.dto.getInternalErrorResponseDto
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus

@ControllerAdvice
class ControllerExceptionHandler {

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ExternalErrorException::class)
    fun externalError(e: ExternalErrorException) = getExternalErrorResponseDto(e)

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(InternalErrorException::class)
    fun internalError(e: InternalErrorException) = getInternalErrorResponseDto(e)
}
