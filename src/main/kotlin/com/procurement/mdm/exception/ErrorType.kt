package com.procurement.mdm.exception

enum class ErrorType constructor(val code: String, val message: String) {
    LANG_UNKNOWN("00.01", "Language not found."),
    COUNTRY_UNKNOWN("00.02", "Country for current language not found."),
    UNIT_CLASS_UNKNOWN("00.03", "Unit class not found."),
    ENTITY_KIND_UNKNOWN("00.03", "Entity kind not found."),
    CPV_CODE_UNKNOWN("00.04", "Cpv code not found."),
    CPVS_CODE_UNKNOWN("00.05", "CPVs code not found.");
}
