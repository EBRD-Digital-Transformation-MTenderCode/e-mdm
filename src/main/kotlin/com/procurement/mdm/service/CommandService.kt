package com.procurement.mdm.service

import com.procurement.mdm.model.dto.CommandMessage
import com.procurement.mdm.model.dto.CommandType
import com.procurement.mdm.model.dto.ResponseDto
import com.procurement.mdm.service.preparation.BidDataService
import com.procurement.mdm.service.preparation.BudgetDataService
import com.procurement.mdm.service.preparation.TenderDataService
import org.springframework.stereotype.Service

interface CommandService {

    fun execute(cm: CommandMessage): ResponseDto

}

@Service
class CommandServiceImpl(private val tenderDataService: TenderDataService,
                         private val budgetDataService: BudgetDataService,
                         private val bidDataService: BidDataService) : CommandService {

    override fun execute(cm: CommandMessage): ResponseDto {
        return when (cm.command) {
            CommandType.CREATE_EI -> budgetDataService.createEi(cm)
            CommandType.CREATE_FS -> budgetDataService.createFs(cm)
            CommandType.CREATE_TENDER -> tenderDataService.createTender(cm)
            CommandType.CREATE_BID -> bidDataService.createBid(cm)
        }
    }
}
