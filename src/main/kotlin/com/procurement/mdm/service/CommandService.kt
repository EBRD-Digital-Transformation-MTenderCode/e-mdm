package com.procurement.mdm.service

import com.procurement.mdm.model.dto.CommandMessage
import com.procurement.mdm.model.dto.CommandType
import com.procurement.mdm.model.dto.ResponseDto
import com.procurement.mdm.service.preparation.TenderDataService
import com.procurement.mdm.service.preparation.BudgetDataService
import com.procurement.mdm.service.preparation.TenderInfoService
import org.springframework.stereotype.Service

interface CommandService {

    fun execute(cm: CommandMessage): ResponseDto

}

@Service
class CommandServiceImpl(private val tenderDataService: TenderDataService,
                         private val tenderInfoService: TenderInfoService,
                         private val budgetDataService: BudgetDataService) : CommandService {

    override fun execute(cm: CommandMessage): ResponseDto {
        return when (cm.command) {
            CommandType.TENDER_CPV -> tenderDataService.tenderCPV(cm)
            CommandType.TENDER_INFO -> tenderInfoService.tenderInfo(cm)
            CommandType.CHECK_CURRENCY -> budgetDataService.checkCurrency(cm)
        }
    }
}
