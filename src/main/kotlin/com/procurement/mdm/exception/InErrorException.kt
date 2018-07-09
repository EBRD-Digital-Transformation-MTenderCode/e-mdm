package com.procurement.mdm.exception

import java.util.*

data class InErrorException(private val error: ErrorType, var id: UUID? = null) : RuntimeException() {

    val code: String = error.code

    val msg: String = error.message

}

