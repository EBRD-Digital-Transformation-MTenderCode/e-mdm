package com.procurement.mdm.model.dto.data

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import java.math.BigDecimal

data class TD @JsonCreator constructor(

        val tender: TenderTD
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class TenderTD @JsonCreator constructor(

        val items: List<ItemTD>?,

        val lots: List<LotTD>?,

        var classification: ClassificationTD?,

        var mainProcurementCategory: String?,

        var submissionMethodRationale: List<String>?,

        var submissionMethodDetails: String?,

        var procurementMethodDetails: String?,

        var eligibilityCriteria: String?,

        val procuringEntity: OrganizationReference?,

        var additionalProcurementCategories: List<String>?
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ItemTD @JsonCreator constructor(

        var id: String?,

        val internalId: String?,

        val description: String?,

        val classification: ClassificationTD,

        val additionalClassifications: List<ClassificationTD>?,

        val quantity: BigDecimal,

        val unit: ItemUnitTD,

        var relatedLot: String
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ClassificationTD @JsonCreator constructor(

        var id: String,

        var description: String?,

        var scheme: String?
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ItemUnitTD @JsonCreator constructor(

        val id: String,

        var name: String?
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class LotTD @JsonCreator constructor(

    val id: String?,

    val internalId: String?,

    val title: String?,

    val description: String?,

    val value: Value?,

    val contractPeriod: ContractPeriod?,

    val placeOfPerformance: PlaceOfPerformance?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("hasOptions") @param:JsonProperty("hasOptions") val hasOptions: Boolean?,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @field:JsonProperty("options") @param:JsonProperty("options") val options: List<Option>?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("hasRecurrence") @param:JsonProperty("hasRecurrence") val hasRecurrence: Boolean?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("recurrence") @param:JsonProperty("recurrence") val recurrence: Recurrence?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("hasRenewal") @param:JsonProperty("hasRenewal") val hasRenewal: Boolean?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("renewal") @param:JsonProperty("renewal") val renewal: Renewal?
) {
    data class Option(
        @JsonInclude(JsonInclude.Include.NON_NULL)
        @field:JsonProperty("description") @param:JsonProperty("description") val description: String?,

        @JsonInclude(JsonInclude.Include.NON_NULL)
        @field:JsonProperty("period") @param:JsonProperty("period") val period: Period?
    )

    data class Recurrence(
        @JsonInclude(JsonInclude.Include.NON_NULL)
        @field:JsonProperty("description") @param:JsonProperty("description") val description: String?,

        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        @field:JsonProperty("dates") @param:JsonProperty("dates") val dates: List<Date>?
    ) {
        data class Date(
            @JsonInclude(JsonInclude.Include.NON_NULL)
            @field:JsonProperty("startDate") @param:JsonProperty("startDate") val startDate: String?
        )
    }

    data class Renewal(
        @JsonInclude(JsonInclude.Include.NON_NULL)
        @field:JsonProperty("description") @param:JsonProperty("description") val description: String?,

        @JsonInclude(JsonInclude.Include.NON_NULL)
        @field:JsonProperty("minimumRenewals") @param:JsonProperty("minimumRenewals") val minimumRenewals: Int?,

        @JsonInclude(JsonInclude.Include.NON_NULL)
        @field:JsonProperty("maximumRenewals") @param:JsonProperty("maximumRenewals") val maximumRenewals: Int?,

        @JsonInclude(JsonInclude.Include.NON_NULL)
        @field:JsonProperty("period") @param:JsonProperty("period") val period: Period?
    )

    data class Period(
        @JsonInclude(JsonInclude.Include.NON_NULL)
        @field:JsonProperty("startDate") @param:JsonProperty("startDate") val startDate: String?,

        @JsonInclude(JsonInclude.Include.NON_NULL)
        @field:JsonProperty("endDate") @param:JsonProperty("endDate") val endDate: String?,

        @JsonInclude(JsonInclude.Include.NON_NULL)
        @field:JsonProperty("durationInDays") @param:JsonProperty("durationInDays") val durationInDays: Int?,

        @JsonInclude(JsonInclude.Include.NON_NULL)
        @field:JsonProperty("maxExtentDate") @param:JsonProperty("maxExtentDate") val maxExtentDate: String?
    )
}


data class Value @JsonCreator constructor(

        var amount: BigDecimal?,

        var currency: String?
)

data class ContractPeriod @JsonCreator constructor(

        val startDate: String?,

        val endDate: String?
)

data class PlaceOfPerformance @JsonCreator constructor(

        val address: Address,

        val description: String?
)
