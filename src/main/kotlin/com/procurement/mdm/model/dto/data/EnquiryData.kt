package com.procurement.mdm.model.dto.data

import com.fasterxml.jackson.annotation.JsonCreator

data class EnquiryData @JsonCreator constructor(

        val author: OrganizationReference
)