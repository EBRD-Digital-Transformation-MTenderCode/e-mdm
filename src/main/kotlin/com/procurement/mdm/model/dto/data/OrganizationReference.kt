package com.procurement.mdm.model.dto.data

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.procurement.mdm.domain.model.enums.TypeOfSupplier
import com.procurement.mdm.model.dto.databinding.JsonDateDeserializer
import com.procurement.mdm.model.dto.databinding.JsonDateSerializer
import java.time.LocalDateTime

@JsonInclude(JsonInclude.Include.NON_NULL)
data class OrganizationReference @JsonCreator constructor(

        var id: String?,

        val name: String?,

        @JsonInclude(JsonInclude.Include.NON_NULL)
        val address: Address?,

        @JsonInclude(JsonInclude.Include.NON_NULL)
        val identifier: Identifier?,

        val additionalIdentifiers: List<Identifier>?,

        val contactPoint: ContactPoint?,

        val details: Details?,

        val buyerProfile: String?,

        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        val persones: List<Persone>?
)

data class Persone @JsonCreator constructor(

        val title: String,

        val name: String,

        val identifier: Identifier,

        val businessFunctions: List<BusinessFunction>
)

data class BusinessFunction @JsonCreator constructor (

        val id: String,

        val type: String,

        val jobTitle: String,

        val period: Period,

        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        val documents: List<Document>?
)

data class Period(
        @JsonDeserialize(using = JsonDateDeserializer::class)
        @JsonSerialize(using = JsonDateSerializer::class)
        val startDate: LocalDateTime
)

data class Document(

        val id: String,

        val documentType: BusinessFunctionDocumentType,

        val title: String,

        @JsonInclude(JsonInclude.Include.NON_NULL)
        val description: String?
)


data class Identifier @JsonCreator constructor(

        val id: String,

        val scheme: String,

        @JsonInclude(JsonInclude.Include.NON_NULL)
        val legalName: String?,

        @JsonInclude(JsonInclude.Include.NON_NULL)
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

        @JsonInclude(JsonInclude.Include.NON_NULL)
        val typeOfSupplier: TypeOfSupplier?,

        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        val mainEconomicActivities: List<MainEconomicActivities>?,

        val typeOfBuyer: TypeOfBuyer?,

        val mainGeneralActivity: MainGeneralActivity?,

        val mainSectoralActivity: MainSectoralActivity?,

        val scale: Scale?,

        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        val permits: List<Permit>?,

        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        val bankAccounts: List<BankAccount>?,

        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        val legalForm: LegalForm?
)

data class MainEconomicActivities(
        val id: String,
        val description: String,
        val scheme: String,

        @JsonInclude(JsonInclude.Include.NON_NULL)
        val uri: String?
)

data class Permit(
        val id: String,
        val scheme: String,

        @JsonInclude(JsonInclude.Include.NON_NULL)
        val url: String?,

        val permitDetails: PermitDetails
)

data class PermitDetails(
        val issuedBy: IssuedBy,
        val issuedThought: IssuedThought,
        val validityPeriod: ValidityPeriod
)


data class IssuedBy(
        val id: String,
        val name: String
)

data class IssuedThought(
        val id: String,
        val name: String
)

data class ValidityPeriod(
        val startDate: LocalDateTime,

        @JsonInclude(JsonInclude.Include.NON_NULL)
        val endDate: LocalDateTime?
)

data class BankAccount(
        val description: String,
        val bankName: String,
        val address: Address,
        val identifier: Identifier,
        val accountIdentification: AccountIdentification,

        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        val additionalAccountIdentifiers: List<AdditionalAccountIdentifier>?
) {
        data class Identifier(
                val scheme: String,
                val id: String
        )

        data class AdditionalAccountIdentifier(
                val id: String,
                val scheme: String
        )

        data class AccountIdentification(
                val scheme: String,
                val id: String
        )
}


data class LegalForm(
        val scheme: String,
        val id: String,
        val description: String,

        @JsonInclude(JsonInclude.Include.NON_NULL)
        val uri: String?
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