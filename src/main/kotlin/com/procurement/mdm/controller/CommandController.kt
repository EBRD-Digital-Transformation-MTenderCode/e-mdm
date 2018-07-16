package com.procurement.mdm.controller

import com.procurement.mdm.model.dto.CommandMessage
import com.procurement.mdm.model.dto.ResponseDto
import com.procurement.mdm.service.CommandService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Validated
@RestController
@RequestMapping("/command")
class CommandController(private val commandService: CommandService) {

    @PostMapping
    fun execute(@RequestBody commandMessage: CommandMessage): ResponseEntity<ResponseDto> {
        return ResponseEntity(commandService.execute(commandMessage), HttpStatus.OK)
    }
}
