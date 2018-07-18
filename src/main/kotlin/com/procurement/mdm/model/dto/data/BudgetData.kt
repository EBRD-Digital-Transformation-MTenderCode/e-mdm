package com.procurement.mdm.model.dto.data

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import javax.validation.Valid
import javax.validation.constraints.NotNull

data class BD @JsonCreator constructor(

        var tender: TenderBD?,

        var planning: PlanningBD?,

        val buyer: OrganizationReference?
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class TenderBD @JsonCreator constructor(

        var classification: ClassificationBD,

        var mainProcurementCategory: String?,

        val procuringEntity: OrganizationReference?
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ClassificationBD @JsonCreator constructor(

        @field:NotNull
        val id: String,

        var description: String?,

        var scheme: String?
)

data class PlanningBD @JsonCreator constructor(

        @field:Valid @field:NotNull
        val budget: BudgetBD,

        var rationale: String?
)

data class BudgetBD @JsonCreator constructor(

        @field:Valid @field:NotNull
        val amount: ValueBD
)

data class ValueBD @JsonCreator constructor(

        @field:NotNull
        val currency: String
)

