package com.procurement.access.model.dto.items

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonValue
import com.procurement.mdm.exception.ErrorType
import com.procurement.mdm.exception.InErrorException
import java.math.BigDecimal
import java.util.*
import javax.validation.Valid
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

data class TenderInfo @JsonCreator constructor(

        @field:NotNull
        @JsonProperty("tender")
        val tender: Tender
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Tender @JsonCreator constructor(

        @field:NotEmpty @field:NotNull
        val items: HashSet<Item>,

        var classification: Classification?,

        var mainProcurementCategory: String?,

        var submissionMethodRationale: List<String>?,

        var submissionMethodDetails: String?,

        var procurementMethodDetails: String?
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Item @JsonCreator constructor(

        var id: String?,

        val description: String?,

        @field:Valid @field:NotNull
        val classification: Classification,

        @field:Valid
        val additionalClassifications: List<Classification>,

        @field:NotNull
        val quantity: BigDecimal,

        @field:Valid @field:NotNull
        val unit: ItemUnit,

        @field:NotNull
        var relatedLot: String
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Classification @JsonCreator constructor(

        @field:NotNull
        val id: String,

        @field:NotNull
        var description: String,

        @field:NotNull
        var scheme: Scheme
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ItemUnit @JsonCreator constructor(

        @field:NotNull
        val id: String,

        @field:NotNull
        var name: String
)

enum class Scheme constructor(private val value: String) {

    CPV("CPV"),
    CPVS("CPVS"),
    GSIN("GSIN"),
    UNSPSC("UNSPSC"),
    CPC("CPC"),
    OKDP("OKDP"),
    OKPD("OKPD");

    override fun toString(): String {
        return this.value
    }

    @JsonValue
    fun value(): String {
        return this.value
    }

    companion object {

        private val CONSTANTS = HashMap<String, Scheme>()

        init {
            for (c in values()) {
                CONSTANTS[c.value] = c
            }
        }

        @JsonCreator
        fun fromValue(value: String): Scheme {
            return CONSTANTS[value] ?: throw InErrorException(ErrorType.INVALID_JSON_TYPE, Scheme::class.java.name)
        }
    }
}