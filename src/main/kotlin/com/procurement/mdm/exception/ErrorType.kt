package com.procurement.mdm.exception

enum class ErrorType constructor(val code: String, val message: String) {
    LANG_UNKNOWN("00.01", "Language not found."),
    COUNTRY_UNKNOWN("00.02", "Country not found.");
}
