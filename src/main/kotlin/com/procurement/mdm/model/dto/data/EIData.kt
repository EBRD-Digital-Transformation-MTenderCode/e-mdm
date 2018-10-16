package com.procurement.mdm.model.dto.data

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude

data class EI @JsonCreator constructor(

        var tender: TenderEI,

        val buyer: OrganizationReference?
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class TenderEI @JsonCreator constructor(

        var classification: ClassificationEI,

        var mainProcurementCategory: String?

)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ClassificationEI @JsonCreator constructor(

        val id: String,

        var description: String?,

        var scheme: String?
)
