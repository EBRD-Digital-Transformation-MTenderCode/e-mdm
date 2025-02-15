package com.procurement.mdm.service

import com.procurement.mdm.model.dto.CommandMessage
import com.procurement.mdm.model.dto.CommandType
import com.procurement.mdm.model.dto.ResponseDto
import com.procurement.mdm.service.preparation.BidDataService
import com.procurement.mdm.service.preparation.BudgetDataService
import com.procurement.mdm.service.preparation.ContractDataService
import com.procurement.mdm.service.preparation.EnquiryDataService
import com.procurement.mdm.service.preparation.TenderDataService
import org.springframework.stereotype.Service

interface CommandService {

    fun execute(cm: CommandMessage): ResponseDto

}

@Service
class CommandServiceImpl(private val tenderDataService: TenderDataService,
                         private val budgetDataService: BudgetDataService,
                         private val bidDataService: BidDataService,
                         private val enquiryDataService: EnquiryDataService,
                         private val contractDataService: ContractDataService) : CommandService {

    override fun execute(cm: CommandMessage): ResponseDto {
        return when (cm.command) {
            CommandType.ENRICH_DATA_FOR_UPDATE_AP -> tenderDataService.enrichDataForUpdateAP(cm)
            CommandType.PROCESS_BID_DATA -> bidDataService.processBidData(cm)
            CommandType.PROCESS_CONTRACT_DATA -> contractDataService.processContractData(cm)
            CommandType.PROCESS_EI_DATA -> budgetDataService.processEiData(cm)
            CommandType.PROCESS_ENQUIRY_DATA -> enquiryDataService.processEnquiryData(cm)
            CommandType.PROCESS_FS_DATA -> budgetDataService.processFsData(cm)
            CommandType.PROCESS_TENDER_DATA -> tenderDataService.processTenderData(cm)
            CommandType.VALIDATE_AP -> tenderDataService.validateAP(cm)
        }
    }
}
