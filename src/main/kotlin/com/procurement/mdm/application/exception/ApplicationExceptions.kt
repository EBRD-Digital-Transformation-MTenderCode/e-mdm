package com.procurement.mdm.application.exception

sealed class ApplicationException(val description: String) : RuntimeException(description)

class CountryNotFoundException : ApplicationException {

    constructor(country: String, language: String) :
        super("The country by code '$country' and language '$language' not found.")

    constructor(language: String) : super("The countries by language '$language' not found.")
}

class RegionNotFoundException(region: String, country: String, language: String) :
    ApplicationException("The region by code '$region', country '$country', language '$language' not found.")

class LocalityNotFoundException(locality: String, country: String, region: String, language: String) :
    ApplicationException("The locality by code '$locality', country '$country', region '$region', language '$language' not found.")

class OrganizationSchemeNotFoundException(country: String) :
    ApplicationException("The organization schemes for country '$country' not found.")

class OrganizationScaleNotFoundException(country: String) :
    ApplicationException("The organization scale for country '$country' not found.")
