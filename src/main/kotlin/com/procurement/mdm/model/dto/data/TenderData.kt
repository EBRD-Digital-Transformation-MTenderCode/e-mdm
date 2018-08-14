package com.procurement.mdm.model.dto.data

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*
import javax.validation.Valid
import javax.validation.constraints.NotNull

data class TD @JsonCreator constructor(

        @field:NotNull
        val tender: TenderTD
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class TenderTD @JsonCreator constructor(

        val items: HashSet<ItemTD>?,

        val lots: HashSet<LotTD>?,

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

        val description: String?,

        @field:Valid @field:NotNull
        val classification: ClassificationTD,

        @field:Valid
        val additionalClassifications: List<ClassificationTD>?,

        @field:NotNull
        val quantity: BigDecimal,

        @field:Valid @field:NotNull
        val unit: ItemUnitTD,

        @field:NotNull
        var relatedLot: String
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ClassificationTD @JsonCreator constructor(

        @field:NotNull
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

        val title: String?,

        val description: String?,

        val value: Value?,

        val contractPeriod: ContractPeriod?,

        val placeOfPerformance: PlaceOfPerformance
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
