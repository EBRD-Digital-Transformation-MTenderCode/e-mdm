package com.procurement.mdm.domain.exception

sealed class DomainException(val description: String) : RuntimeException(description)

class InvalidLanguageCodeException(description: String) : DomainException(description)

class InvalidCountryCodeException(description: String) : DomainException(description)

class InvalidCountrySchemeException(description: String) : DomainException(description)

class InvalidRegionCodeException(description: String) : DomainException(description)

class InvalidRegionSchemeException(description: String) : DomainException(description)

class InvalidLocalityCodeException(description: String) : DomainException(description)

class LanguageUnknownException(language: String) : DomainException("The unknown code of a language '$language'.")

class CountryUnknownException(country: String) : DomainException("The unknown code of a country '$country'.")
