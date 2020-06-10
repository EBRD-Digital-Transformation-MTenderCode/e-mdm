package com.procurement.mdm.application.exception

import com.procurement.mdm.domain.model.code.CountryCode
import com.procurement.mdm.domain.model.code.LanguageCode
import com.procurement.mdm.domain.model.scheme.CountryScheme

sealed class ApplicationException(val description: String) : RuntimeException(description)

class CountryNotFoundException : ApplicationException {

    constructor(country: CountryCode, language: LanguageCode) :
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

class SchemeNotFoundException(scheme: CountryScheme) :
    ApplicationException("Scheme '$scheme' not found.")

class IdNotFoundException : ApplicationException{
    constructor(country: CountryCode, scheme: CountryScheme) :
        super("Country id '$country' by scheme '$scheme' not found.")
}
