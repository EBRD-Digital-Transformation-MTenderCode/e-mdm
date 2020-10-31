package com.procurement.mdm.infrastructure.web.controller.classification

import com.fasterxml.jackson.annotation.JsonProperty
import com.procurement.mdm.application.service.classification.ClassificationService
import com.procurement.mdm.infrastructure.exception.LanguageRequestParameterMissingException
import com.procurement.mdm.infrastructure.exception.SchemeRequestParameterMissingException
import com.procurement.mdm.infrastructure.web.dto.ApiResponse
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/classifications")
class ClassificationController(private val classificationService: ClassificationService) {

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun getClassificationById(
        @RequestParam(value = "lang", required = false) lang: String?,
        @RequestParam(value = "scheme", required = false) scheme: String?,
        @PathVariable(value = "id") id: String
    ): ClassificationApiResponse {
        if (lang == null)
            throw LanguageRequestParameterMissingException()
        if (scheme == null)
            throw SchemeRequestParameterMissingException()

        return classificationService.getById(id = id, language = lang, scheme = scheme)
            .let { classification ->
                Classification(
                    id = classification.id,
                    scheme = scheme,
                    description = classification.description
                )
            }.let { ClassificationApiResponse(it) }
    }

    class ClassificationApiResponse(classification: Classification) : ApiResponse<Classification>(classification)

    data class Classification(
        @field:JsonProperty("id") @param:JsonProperty("id") val id: String,
        @field:JsonProperty("scheme") @param:JsonProperty("scheme") val scheme: String,
        @field:JsonProperty("description") @param:JsonProperty("description") val description: String
    )
}
