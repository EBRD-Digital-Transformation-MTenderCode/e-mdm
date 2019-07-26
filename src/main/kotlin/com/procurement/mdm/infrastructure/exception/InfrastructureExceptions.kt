package com.procurement.mdm.infrastructure.exception

sealed class InfrastructureException(val description: String) : RuntimeException(description)

class LanguageRequestParameterMissingException :
    InfrastructureException("The request is missing a required query parameter - 'lang'.")

class CountryRequestParameterMissingException :
    InfrastructureException("The request is missing a required query parameter - 'country'.")
