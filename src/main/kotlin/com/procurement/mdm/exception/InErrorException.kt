package com.procurement.mdm.exception

data class InErrorException(private val error: ErrorType, var id: String? = null) : RuntimeException() {

    val code: String = error.code

    val msg: String = error.message

}

