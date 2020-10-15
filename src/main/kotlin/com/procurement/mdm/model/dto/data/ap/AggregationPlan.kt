package com.procurement.mdm.model.dto.data.ap

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.procurement.mdm.model.dto.data.ClassificationTD
import com.procurement.mdm.model.dto.data.ContractPeriod
import com.procurement.mdm.model.dto.data.Document
import com.procurement.mdm.model.dto.data.OrganizationReference
import com.procurement.mdm.model.dto.data.Period
import com.procurement.mdm.model.dto.data.Value

data class AggregationPlan(
    @field:JsonProperty("tender") @param:JsonProperty("tender") val tender: ApTender
)

data class ApTender(
    @field:JsonProperty("title") @param:JsonProperty("title") val title: String,

    @field:JsonProperty("description") @param:JsonProperty("description") val description: String,

    @field:JsonProperty("legalBasis") @param:JsonProperty("legalBasis") val legalBasis: String,

    @field:JsonProperty("contractPeriod") @param:JsonProperty("contractPeriod") val contractPeriod: ContractPeriod,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("procurementMethodRationale") @param:JsonProperty("procurementMethodRationale") val procurementMethodRationale: String?,

    @field:JsonProperty("classification") @param:JsonProperty("classification") val classification: ClassificationTD,

    @field:JsonProperty("tenderPeriod") @param:JsonProperty("tenderPeriod") val tenderPeriod: Period,

    @field:JsonProperty("procuringEntity") @param:JsonProperty("procuringEntity") val procuringEntity: OrganizationReference,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @field:JsonProperty("documents") @param:JsonProperty("documents") val documents: List<Document>?,

    @field:JsonProperty("submissionMethodRationale") @param:JsonProperty("submissionMethodRationale") val submissionMethodRationale: List<String>?,

    @field:JsonProperty("submissionMethodDetails") @param:JsonProperty("submissionMethodDetails") val submissionMethodDetails: String?,

    @field:JsonProperty("procurementMethodDetails") @param:JsonProperty("procurementMethodDetails") val procurementMethodDetails: String?,

    @field:JsonProperty("eligibilityCriteria") @param:JsonProperty("eligibilityCriteria") val eligibilityCriteria: String?,

    @field:JsonProperty("value") @param:JsonProperty("value") val value: Value
)