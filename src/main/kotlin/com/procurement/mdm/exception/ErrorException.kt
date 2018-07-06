package com.procurement.mdm.exception


data class ErrorException(private val error: ErrorType) : RuntimeException() {

    val code: String = error.code

    val msg: String = error.message
}
