package com.procurement.mdm.model.dto.data

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import java.math.BigDecimal

data class CD @JsonCreator constructor(

        val items: List<ItemCD>
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ItemCD @JsonCreator constructor(

        val id: String,

        val quantity: BigDecimal,

        val unit: UnitUpdate,

        val deliveryAddress: Address
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class UnitUpdate @JsonCreator constructor(

        val value: ValueUpdate
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ValueUpdate @JsonCreator constructor(

        val amount: BigDecimal,

        val currency: String,

        val amountNet: BigDecimal,

        val valueAddedTaxIncluded: Boolean
)