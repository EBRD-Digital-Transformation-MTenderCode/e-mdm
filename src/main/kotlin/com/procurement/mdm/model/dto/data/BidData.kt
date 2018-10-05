package com.procurement.mdm.model.dto.data

import com.fasterxml.jackson.annotation.JsonCreator

data class BidData @JsonCreator constructor(

        val tenderers: List<OrganizationReference>
)