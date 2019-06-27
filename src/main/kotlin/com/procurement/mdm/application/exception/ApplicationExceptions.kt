package com.procurement.mdm.application.exception

sealed class ApplicationException(val description: String) : RuntimeException(description)

class CountryNotFoundException : ApplicationException {

    constructor(country: String, language: String) :
        super("The country by code '$country' and language '$language' not found.")

    constructor(language: String) : super("The countries by language '$language' not found.")
}

class RegionNotFoundException(region: String, country: String, language: String) :
    ApplicationException("The region by code '$region', country '$country', language '$language' not found.")
