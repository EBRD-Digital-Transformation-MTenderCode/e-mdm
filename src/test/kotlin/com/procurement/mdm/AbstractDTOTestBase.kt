package com.procurement.mdm

import com.procurement.mdm.json.testMappingOnFail
import com.procurement.mdm.json.testingBindingAndMapping

abstract class AbstractDTOTestBase<T : Any>(private val target: Class<T>) {
    fun testBindingAndMapping(pathToJsonFile: String) {
        testingBindingAndMapping(pathToJsonFile, target)
    }

    fun testMappingOnFail(pathToJsonFile: String) {
        testMappingOnFail(pathToJsonFile, target)
    }
}
