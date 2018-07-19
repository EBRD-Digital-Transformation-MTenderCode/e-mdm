package com.procurement.mdm.controller

import com.procurement.mdm.exception.InErrorException
import com.procurement.mdm.model.dto.CommandMessage
import com.procurement.mdm.model.dto.ResponseDto
import com.procurement.mdm.model.dto.getExceptionResponseDto
import com.procurement.mdm.model.dto.getInErrorResponseDto
import com.procurement.mdm.service.CommandService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@Validated
@RestController
@RequestMapping("/command")
class CommandController(private val commandService: CommandService) {

    @PostMapping
    fun execute(@RequestBody commandMessage: CommandMessage): ResponseEntity<ResponseDto> {
        return ResponseEntity(commandService.execute(commandMessage), HttpStatus.OK)
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(Exception::class)
    fun exception(ex: Exception) = getExceptionResponseDto(ex)

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(InErrorException::class)
    fun internalError(e: InErrorException) = getInErrorResponseDto(e)
}


