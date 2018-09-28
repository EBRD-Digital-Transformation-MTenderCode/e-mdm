package com.procurement.mdm.model.dto.data

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import javax.validation.Valid
import javax.validation.constraints.NotNull

data class FS @JsonCreator constructor(

        var tender: TenderFS,

        var planning: PlanningFS,

        val buyer: OrganizationReference?
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class TenderFS @JsonCreator constructor(

        val procuringEntity: OrganizationReference?
)

data class PlanningFS @JsonCreator constructor(

        val budget: BudgetFS
)

data class BudgetFS @JsonCreator constructor(

        val amount: ValueFS
)

data class ValueFS @JsonCreator constructor(

        val currency: String
)

