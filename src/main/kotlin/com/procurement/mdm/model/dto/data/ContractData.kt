package com.procurement.mdm.model.dto.data

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude

data class CD @JsonCreator constructor(

        val items: List<ItemCD>
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ItemCD @JsonCreator constructor(

        var id: String,

        var deliveryAddress: Address
)