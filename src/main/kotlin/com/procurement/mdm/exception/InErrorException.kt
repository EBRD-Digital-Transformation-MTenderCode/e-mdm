package com.procurement.mdm.exception

data class InErrorException(private val error: ErrorType,
                            var desc: String? = null,
                            var id: String? = null) :
        RuntimeException() {

    val code: String = error.code

    val msg: String = error.message + " " + desc

}

