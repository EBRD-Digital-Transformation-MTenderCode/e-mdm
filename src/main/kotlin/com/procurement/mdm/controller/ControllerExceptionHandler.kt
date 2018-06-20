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

//    @ResponseBody
//    @ResponseStatus(OK)
//    @ExceptionHandler(NullPointerException::class)
//    fun nullPointer(e: NullPointerException) =
//            ResponseDto(false, getErrors("NullPointerException", "NullPointerException"), null)
//
//    @ResponseBody
//    @ResponseStatus(OK)
//    @ExceptionHandler(MethodArgumentNotValidException::class)
//    fun methodArgumentNotValid(e: MethodArgumentNotValidException) =
//            ResponseDto(false, getErrors(e.bindingResult), null)
//
//    @ResponseBody
//    @ResponseStatus(OK)
//    @ExceptionHandler(ConstraintViolationException::class)
//    fun constraintViolation(e: ConstraintViolationException) =
//            ResponseDto(false, getErrors(e), null)
//
//    @ResponseBody
//    @ResponseStatus(OK)
//    @ExceptionHandler(JsonMappingException::class)
//    fun jsonMapping(e: JsonMappingException) =
//            ResponseDto(false, getErrors("JsonMappingException", e.message), null)
//
//    @ResponseBody
//    @ResponseStatus(OK)
//    @ExceptionHandler(IllegalArgumentException::class)
//    fun illegalArgument(e: IllegalArgumentException) =
//            ResponseDto(false, getErrors("IllegalArgumentException", e.message), null)
//
//
//    @ResponseBody
//    @ResponseStatus(OK)
//    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
//    fun methodArgumentTypeMismatch(e: MethodArgumentTypeMismatchException) =
//            ResponseDto(false, getErrors("MethodArgumentTypeMismatchException", e.message), null)
//
//    @ResponseBody
//    @ResponseStatus(OK)
//    @ExceptionHandler(ServletException::class)
//    fun servlet(e: ServletException) =
//            ResponseDto(false, getErrors("ServletException", e.message), null)
//
//    @ResponseBody
//    @ResponseStatus(OK)
//    @ExceptionHandler(ErrorException::class)
//    fun error(e: ErrorException) = ResponseDto(false, getErrors(e.code, e.msg), null)
//


//
//    private fun getErrors(result: BindingResult) =
//            result.fieldErrors.asSequence()
//                    .map {
//                        ResponseDetailsDto(code = ERROR_PREFIX + it.field, message = """${it.code} : ${it
//                                .defaultMessage}""")
//                    }
//                    .toList()
//
//    private fun getErrors(e: ConstraintViolationException) =
//            e.constraintViolations.asSequence()
//                    .map {
//                        ResponseDetailsDto(
//                                code = ERROR_PREFIX + it.propertyPath.toString(),
//                                message = """${it.message} ${it.messageTemplate}""")
//                    }
//                    .toList()
//
//
//    private fun getErrors(code: String, error: String?) =
//            listOf(ResponseDetailsDto(code = ERROR_PREFIX + code, message = error!!))
//
//    companion object {
//        private val ERROR_PREFIX = "400.10."
//    }
}
