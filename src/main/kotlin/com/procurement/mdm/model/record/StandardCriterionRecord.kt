package com.procurement.mdm.model.record

import com.procurement.mdm.model.entity.StandardCriterionEntity
import com.procurement.mdm.utils.toObject

data class StandardCriterionRecord(
    val id: String,
    val country: String,
    val language: String,
    val data: String
) {
    companion object {

        fun toEntity(entity: StandardCriterionRecord): StandardCriterionEntity =
            StandardCriterionEntity(
                id = entity.id,
                country = entity.country,
                language = entity.language,
                criterion = toObject(StandardCriterionEntity.StandardCriterion::class.java, entity.data)
            )
    }
}