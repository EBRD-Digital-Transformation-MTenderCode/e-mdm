package com.procurement.mdm.infrastructure.exception

sealed class InfrastructureException(val description: String) : RuntimeException(description)

class LanguageRequestParameterMissingException :
    InfrastructureException("The request is missing a required query parameter - 'lang'.")

class CountryRequestParameterMissingException :
    InfrastructureException("The request is missing a required query parameter - 'country'.")

class RequestPayloadMissingException :
    InfrastructureException("The request is missing a required payload.")

class NoHandlerUrlException(url: String) :
    InfrastructureException("Unknown url: '$url'.")

class PmdRequestParameterMissingException :
    InfrastructureException("The request is missing a required query parameter - 'pmd'.")

class PhaseRequestParameterMissingException :
    InfrastructureException("The request is missing a required query parameter - 'phase'.")

class CriterionRequestParameterMissingException :
    InfrastructureException("The request is missing a required query parameter - 'criterionId'.")

class RequirementGroupIdParameterMissingException:
    InfrastructureException("The request is missing a required query parameter - 'requirementGroupId'.")

class SchemeRequestParameterMissingException :
    InfrastructureException("The request is missing a required query parameter - 'scheme'.")
