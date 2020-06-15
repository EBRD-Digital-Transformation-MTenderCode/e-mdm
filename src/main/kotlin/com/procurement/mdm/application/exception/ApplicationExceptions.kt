package com.procurement.mdm.application.exception

import com.procurement.mdm.domain.model.code.CountryCode
import com.procurement.mdm.domain.model.code.LanguageCode
import com.procurement.mdm.domain.model.code.LocalityCode
import com.procurement.mdm.domain.model.code.RegionCode
import com.procurement.mdm.domain.model.scheme.CountryScheme
import com.procurement.mdm.domain.model.scheme.LocalityScheme
import com.procurement.mdm.domain.model.scheme.RegionScheme

sealed class ApplicationException(val description: String) : RuntimeException(description)

class CountryNotFoundException : ApplicationException {

    constructor(country: CountryCode, language: LanguageCode) :
        super("The country by code '$country' and language '$language' not found.")

    constructor(language: String) : super("The countries by language '$language' not found.")

    constructor(country: CountryCode, scheme: CountryScheme) :
        super("The country by code '$country' and scheme '$scheme' not found.")
}

class RegionNotFoundException : ApplicationException {
    constructor(region: RegionCode, country: CountryCode, language: LanguageCode) :
        super("The region by code '$region', country '$country', language '$language' not found.")

    constructor(region: RegionCode, scheme: RegionScheme, country: CountryCode, language: LanguageCode) :
        super("The region by code '$region', scheme '$scheme', country '$country', language '$language' not found.")

    constructor(region: RegionCode, scheme: RegionScheme) :
        super("The region by code '$region' and scheme '$scheme' not found.")
}

class RegionNotLinkedToCountryException : ApplicationException {
    constructor(region: RegionCode, scheme: RegionScheme, country: CountryCode) :
        super("The region by code '$region' and scheme '$scheme' is not linked to country '$country'.")

    constructor(region: RegionCode, country: CountryCode) :
        super("The region by code '$region' is not linked to country '$country'.")
}

class LocalityNotFoundException : ApplicationException {
    constructor(locality: LocalityCode, country: CountryCode, region: RegionCode, language: LanguageCode) :
        super("The locality by code '$locality', country '$country', region '$region', language '$language' not found.")

    constructor(locality: LocalityCode, scheme: LocalityScheme) :
        super("The locality by code '$locality' and scheme '$scheme' not found.")

    constructor(
        locality: LocalityCode,
        scheme: LocalityScheme,
        country: CountryCode,
        region: RegionCode,
        language: LanguageCode
    ) :
        super("The locality by code '$locality', scheme '$scheme', country '$country', region '$region', language '$language' not found.")
}

class LocalityNotLinkedToRegionException(locality: LocalityCode, scheme: LocalityScheme, region: RegionCode) :
    ApplicationException("The locality by code '$locality' and scheme '$scheme' is not linked to region '$region'.")

class OrganizationSchemeNotFoundException(country: String) :
    ApplicationException("The organization schemes for country '$country' not found.")

class OrganizationScaleNotFoundException(country: String) :
    ApplicationException("The organization scale for country '$country' not found.")

class CountrySchemeNotFoundException (scheme: CountryScheme) : ApplicationException("Country scheme '$scheme' not found.")

class RegionSchemeNotFoundException (scheme: RegionScheme) : ApplicationException("Region scheme '$scheme' not found.")

class LocalitySchemeNotFoundException (scheme: LocalityScheme) : ApplicationException("Locality scheme '$scheme' not found.")


