package com.procurement.mdm.model.dto.data

import java.math.BigDecimal

data class EIData(
    var tender: Tender,
    val buyer: OrganizationReference?
) {
    data class Tender(
        var classification: Classification,

        val items: List<Item>
    ) {
        data class Classification(
            val id: String
        )

        data class Item(
            val id: String,
            val description: String,
            val classification: Classification,
            val additionalClassifications: List<AdditionalClassification>,
            val deliveryAddress: DeliveryAddress,
            val quantity: BigDecimal,
            val unit: Unit
        ) {
            data class Classification(
                val id: String,
                val description: String?,
                val scheme: String?
            )

            data class AdditionalClassification(
                val id: String,
                val description: String?,
                val scheme: String?
            )

            data class DeliveryAddress(
                val countryName: String,
                val region: String,
                val locality: String,
                val streetAddress: String,
                val postalCode: String?
            )

            data class Unit(
                val id: String,
                val name: String?
            )
        }
    }
}

