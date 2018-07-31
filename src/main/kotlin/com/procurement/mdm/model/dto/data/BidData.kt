package com.procurement.mdm.model.dto.data

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import java.math.BigDecimal
import java.util.*
import javax.validation.Valid
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

data class BidData @JsonCreator constructor(

        @field:Valid @field:NotEmpty @field:NotNull
        val tenderers: List<OrganizationReference>
)