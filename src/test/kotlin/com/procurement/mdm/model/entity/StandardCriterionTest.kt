package com.procurement.mdm.model.entity

import com.procurement.mdm.AbstractDTOTestBase
import org.junit.jupiter.api.Test

class StandardCriterionTest :
    AbstractDTOTestBase<StandardCriterionEntity.StandardCriterion>(StandardCriterionEntity.StandardCriterion::class.java) {

    @Test
    fun fully() {
        testBindingAndMapping("json/model/entity/standard_criterion_fully.json")
    }

    @Test
    fun required1() {
        testBindingAndMapping("json/model/entity/standard_criterion_required_1.json")
    }
}
