package com.procurement.mdm.exception


data class ExternalErrorException(private val error: ErrorType) : RuntimeException() {
    val code: String = error.code
    val msg: String = error.message
}
