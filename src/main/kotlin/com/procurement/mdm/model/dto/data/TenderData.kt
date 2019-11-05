package com.procurement.mdm.model.dto.data

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import java.math.BigDecimal
import java.util.*

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

        val procuringEntity: OrganizationReference?
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

data class LotTD @JsonCreator constructor(

        var id: String?,

        val internalId: String?,

        val title: String?,

        val description: String?,

        val value: Value?,

        val contractPeriod: ContractPeriod?,

        val placeOfPerformance: PlaceOfPerformance?
)

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
