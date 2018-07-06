package com.procurement.mdm.exception

import java.util.*


data class ErrorException(private val error: ErrorType, var id: UUID? = null) : RuntimeException() {

    val code: String = error.code

    val msg: String = error.message

}
