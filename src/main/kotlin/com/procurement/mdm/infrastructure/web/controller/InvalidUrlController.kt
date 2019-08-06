package com.procurement.mdm.infrastructure.web.controller

import com.procurement.mdm.infrastructure.exception.NoHandlerUrlException
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.NoHandlerFoundException
import javax.servlet.http.HttpServletRequest

@RestController
class InvalidUrlController {

    @Throws(NoHandlerFoundException::class)
    @RequestMapping("/**")
    fun handlerNotMappingRequest(request: HttpServletRequest): Unit = throw NoHandlerUrlException(request.requestURI)
}
