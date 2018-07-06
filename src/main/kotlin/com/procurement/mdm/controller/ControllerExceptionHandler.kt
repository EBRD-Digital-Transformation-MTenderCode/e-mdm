package com.procurement.mdm.controller

import com.procurement.mdm.exception.ErrorException
import com.procurement.mdm.model.dto.getErrorResponseDto
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus

@ControllerAdvice
class ControllerExceptionHandler {

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ErrorException::class)
    fun error(e: ErrorException) = getErrorResponseDto(e)

}
