package com.procurement.mdm.model.dto.data.ei

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import java.math.BigDecimal

data class EIResponse(
    @param:JsonProperty("tender") @field:JsonProperty("tender") val tender: Tender,
    @param:JsonProperty("buyer") @field:JsonProperty("buyer") val buyer: Buyer
) {
    data class Tender(
        @param:JsonProperty("classification") @field:JsonProperty("classification") val classification: Classification,
        @param:JsonProperty("mainProcurementCategory") @field:JsonProperty("mainProcurementCategory") val mainProcurementCategory: String,

        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        @param:JsonProperty("items") @field:JsonProperty("items") val items: List<Item>?
    ) {
        data class Classification(
            @param:JsonProperty("id") @field:JsonProperty("id") val id: String,
            @param:JsonProperty("description") @field:JsonProperty("description") val description: String,
            @param:JsonProperty("scheme") @field:JsonProperty("scheme") val scheme: String
        )

        data class Item(
            @param:JsonProperty("id") @field:JsonProperty("id") val id: String,
            @param:JsonProperty("description") @field:JsonProperty("description") val description: String,
            @param:JsonProperty("classification") @field:JsonProperty("classification") val classification: Classification,

            @JsonInclude(JsonInclude.Include.NON_EMPTY)
            @param:JsonProperty("additionalClassifications") @field:JsonProperty("additionalClassifications") val additionalClassifications: List<AdditionalClassification>?,

            @param:JsonProperty("deliveryAddress") @field:JsonProperty("deliveryAddress") val deliveryAddress: DeliveryAddress,
            @param:JsonProperty("quantity") @field:JsonProperty("quantity") val quantity: BigDecimal,
            @param:JsonProperty("unit") @field:JsonProperty("unit") val unit: Unit
        ) {
            data class Classification(
                @param:JsonProperty("id") @field:JsonProperty("id") val id: String,
                @param:JsonProperty("description") @field:JsonProperty("description") val description: String,
                @param:JsonProperty("scheme") @field:JsonProperty("scheme") val scheme: String
            )

            data class AdditionalClassification(
                @param:JsonProperty("id") @field:JsonProperty("id") val id: String,
                @param:JsonProperty("description") @field:JsonProperty("description") val description: String,
                @param:JsonProperty("scheme") @field:JsonProperty("scheme") val scheme: String
            )

            data class DeliveryAddress(
                @param:JsonProperty("countryName") @field:JsonProperty("countryName") val countryName: String,
                @param:JsonProperty("region") @field:JsonProperty("region") val region: String,
                @param:JsonProperty("locality") @field:JsonProperty("locality") val locality: String,
                @param:JsonProperty("streetAddress") @field:JsonProperty("streetAddress") val streetAddress: String,

                @JsonInclude(JsonInclude.Include.NON_NULL)
                @param:JsonProperty("postalCode") @field:JsonProperty("postalCode") val postalCode: String?
            )

            data class Unit(
                @param:JsonProperty("id") @field:JsonProperty("id") val id: String,
                @param:JsonProperty("name") @field:JsonProperty("name") val name: String
            )
        }
    }

    data class Buyer(
        @param:JsonProperty("name") @field:JsonProperty("name") val name: String,
        @param:JsonProperty("address") @field:JsonProperty("address") val address: Address,
        @param:JsonProperty("identifier") @field:JsonProperty("identifier") val identifier: Identifier,
        @param:JsonProperty("contactPoint") @field:JsonProperty("contactPoint") val contactPoint: ContactPoint
    ) {
        data class Address(
            @param:JsonProperty("streetAddress") @field:JsonProperty("streetAddress") val streetAddress: String,
            @param:JsonProperty("addressDetails") @field:JsonProperty("addressDetails") val addressDetails: AddressDetails
        ) {
            data class AddressDetails(
                @param:JsonProperty("country") @field:JsonProperty("country") val country: Country,
                @param:JsonProperty("region") @field:JsonProperty("region") val region: Region,
                @param:JsonProperty("locality") @field:JsonProperty("locality") val locality: Locality
            ) {
                data class Country(
                    @param:JsonProperty("scheme") @field:JsonProperty("scheme") val scheme: String,
                    @param:JsonProperty("id") @field:JsonProperty("id") val id: String,
                    @param:JsonProperty("description") @field:JsonProperty("description") val description: String,
                    @param:JsonProperty("uri") @field:JsonProperty("uri") val uri: String
                )

                data class Region(
                    @param:JsonProperty("scheme") @field:JsonProperty("scheme") val scheme: String,
                    @param:JsonProperty("id") @field:JsonProperty("id") val id: String,
                    @param:JsonProperty("description") @field:JsonProperty("description") val description: String,
                    @param:JsonProperty("uri") @field:JsonProperty("uri") val uri: String
                )

                data class Locality(
                    @param:JsonProperty("scheme") @field:JsonProperty("scheme") val scheme: String,
                    @param:JsonProperty("id") @field:JsonProperty("id") val id: String,
                    @param:JsonProperty("description") @field:JsonProperty("description") val description: String,
                    @param:JsonProperty("uri") @field:JsonProperty("uri") val uri: String
                )
            }
        }

        data class Identifier(
            @param:JsonProperty("id") @field:JsonProperty("id") val id: String,
            @param:JsonProperty("scheme") @field:JsonProperty("scheme") val scheme: String,
            @param:JsonProperty("legalName") @field:JsonProperty("legalName") val legalName: String,
            @param:JsonProperty("uri") @field:JsonProperty("uri") val uri: String
        )

        data class ContactPoint(
            @param:JsonProperty("name") @field:JsonProperty("name") val name: String,
            @param:JsonProperty("email") @field:JsonProperty("email") val email: String,
            @param:JsonProperty("telephone") @field:JsonProperty("telephone") val telephone: String,
            @param:JsonProperty("faxNumber") @field:JsonProperty("faxNumber") val faxNumber: String,
            @param:JsonProperty("url") @field:JsonProperty("url") val url: String
        )
    }
}
