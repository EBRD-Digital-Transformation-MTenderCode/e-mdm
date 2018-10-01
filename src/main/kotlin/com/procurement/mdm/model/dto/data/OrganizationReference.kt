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

        val address: Address,

        val identifier: Identifier,

        val additionalIdentifiers: HashSet<Identifier>?,

        val contactPoint: ContactPoint?,

        val details: Details?,

        val buyerProfile: String?
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Identifier @JsonCreator constructor(

        val id: String,

        val scheme: String,

        val legalName: String,

        val uri: String?
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ContactPoint @JsonCreator constructor(

        val name: String,

        val email: String,

        val telephone: String,

        val faxNumber: String?,

        val url: String?
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Details @JsonCreator constructor(

        val typeOfBuyer: TypeOfBuyer?,

        val mainGeneralActivity: MainGeneralActivity?,

        val mainSectoralActivity: MainSectoralActivity?,

        val scale: Scale?
)


@JsonInclude(JsonInclude.Include.NON_NULL)
data class Address @JsonCreator constructor(

        val streetAddress: String,

        val postalCode: String?,

        val addressDetails: AddressDetails
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class AddressDetails(

        val country: CountryDetails,

        val region: RegionDetails,

        val locality: LocalityDetails
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class CountryDetails(

        var scheme: String?,

        val id: String,

        var description: String?,

        var uri: String?
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class RegionDetails(

        var scheme: String?,

        val id: String,

        var description: String?,

        var uri: String?
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class LocalityDetails(

        var scheme: String,

        val id: String,

        var description: String,

        var uri: String?
)