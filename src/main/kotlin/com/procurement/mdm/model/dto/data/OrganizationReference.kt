package com.procurement.mdm.model.dto.data

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import java.util.HashSet
import javax.validation.Valid
import javax.validation.constraints.NotNull

@JsonInclude(JsonInclude.Include.NON_NULL)
data class OrganizationReference @JsonCreator constructor(

        var id: String?,

        val name: String?,

        @field:Valid
        val address: Address,

        @field:Valid
        val identifier: Identifier,

        @field:Valid
        val additionalIdentifiers: HashSet<Identifier>?,

        @field:Valid
        val contactPoint: ContactPoint?,

        @field:Valid
        val details: Details?,

        val buyerProfile: String?
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Identifier @JsonCreator constructor(

        @field:NotNull
        val id: String,

        @field:NotNull
        val scheme: String,

        @field:NotNull
        val legalName: String,

        val uri: String?
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ContactPoint @JsonCreator constructor(

        @field:NotNull
        val name: String,

        @field:NotNull
        val email: String,

        @field:NotNull
        val telephone: String,

        val faxNumber: String?,

        @field:NotNull
        val url: String
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Details @JsonCreator constructor(

        @field:NotNull
        val typeOfBuyer: TypeOfBuyer,

        @field:NotNull
        val mainGeneralActivity: MainGeneralActivity,

        @field:NotNull
        val mainSectoralActivity: MainSectoralActivity,

        val scale: Scale?
)


@JsonInclude(JsonInclude.Include.NON_NULL)
data class Address @JsonCreator constructor(

        @field:NotNull
        val streetAddress: String,

        val postalCode: String?,

        @field:Valid @field:NotNull
        val addressDetails: AddressDetails
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class AddressDetails(

        @field:Valid @field:NotNull
        val country: CountryDetails,

        @field:Valid @field:NotNull
        val region: RegionDetails,

        @field:Valid @field:NotNull
        val locality: LocalityDetails
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class CountryDetails(

        var scheme: String?,

        @field:NotNull
        val id: String,

        var description: String?,

        var uri: String?
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class RegionDetails(

        var scheme: String?,

        @field:NotNull
        val id: String,

        var description: String?,

        var uri: String?
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class LocalityDetails(

        @field:NotNull
        var scheme: String,

        @field:NotNull
        val id: String,

        @field:NotNull
        var description: String,

        var uri: String?
)