package com.procurement.mdm.infrastructure.web.controller.requirement

import com.procurement.mdm.application.service.requirement.RequirementService
import com.procurement.mdm.application.service.requirement.RequirementServiceImpl
import com.procurement.mdm.domain.entity.RequirementEntity
import com.procurement.mdm.domain.repository.requirement.RequirementRepository
import com.procurement.mdm.infrastructure.repository.AbstractRepositoryTest
import com.procurement.mdm.infrastructure.repository.loadSql
import com.procurement.mdm.infrastructure.web.controller.RestExceptionHandler
import com.procurement.mdm.infrastructure.web.controller.documentation.ModelDescription
import com.procurement.mdm.infrastructure.web.dto.ErrorCode
import com.procurement.mdm.infrastructure.web.dto.ErrorCode.LANGUAGE_REQUEST_PARAMETER_MISSING
import org.hamcrest.core.IsEqual.equalTo
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.RestDocumentationExtension
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration
import org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.test.web.servlet.setup.StandaloneMockMvcBuilder

@ExtendWith(RestDocumentationExtension::class)
class RequirementControllerIT : AbstractRepositoryTest() {
    companion object {
        private const val LANGUAGE = "en"
        private const val UNKNOWN_LANGUAGE = "fr"

        private const val COUNTRY = "md"
        private const val UNKNOWN_COUNTRY = "uk"

        private const val PMD = "ot"
        private const val UNKNOWN_PMD = "gpa"

        private const val PHASE = "submission"
        private val UNKNOWN_PHASE = "awarding"

        private const val REQ_GROUP = "req_group_1"
        private const val REQ_GROUP_NO_DESCRIPTION = "req_group_2"
        private const val UNKNOWN_REQ_GROUP = "unknown_req_group_3"

        private const val EMPTY_PARAMETER = "  "

        private val FIRST_REQ_IDENTIFIER = RequirementEntity(
            id = "REQ_1",
            title = "req-title-1",
            description = "req-description-1"
        )

        private val SECOND_REQ_IDENTIFIER = RequirementEntity(
            id = "REQ_1_1",
            title = "req-title-1-1",
            description = "req-description-1-1"
        )

        private val THIRD_REQ_IDENTIFIER = RequirementEntity(
            id = "REQ_2",
            title = "req-title-2",
            description = null
        )
    }

    private lateinit var mockMvc: MockMvc
    private lateinit var requirementService: RequirementService

    @Autowired
    private lateinit var requirementRepository: RequirementRepository

    @BeforeEach
    fun init(restDocumentation: RestDocumentationContextProvider) {
        requirementService = RequirementServiceImpl(requirementRepository)

        val controller = RequirementController(requirementService)
        val restExceptionHandler = RestExceptionHandler()
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
            .setControllerAdvice(restExceptionHandler)
            .apply<StandaloneMockMvcBuilder>(
                documentationConfiguration(restDocumentation)
                    .uris()
                    .withScheme("https")
                    .withHost("eprocurement.systems")
                    .and()
                    .snippets()
                    .and()
                    .operationPreprocessors()
                    .withRequestDefaults(prettyPrint())
                    .withResponseDefaults(prettyPrint())
            )
            .build()
    }

    @Test
    fun `Getting all requirements is successful`() {
        initData()

        val url = getUrl()
        mockMvc.perform(
            get(url)
                .param("lang", LANGUAGE)
                .param("country", COUNTRY)
                .param("pmd", PMD)
                .param("phase", PHASE)
                .param("requirementGroupId", REQ_GROUP)
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType("application/json;charset=UTF-8"))
            .andExpect(jsonPath("$.data.length()", equalTo(2)))
            .andExpect(jsonPath("$.data[0].id", equalTo(FIRST_REQ_IDENTIFIER.id)))
            .andExpect(jsonPath("$.data[0].description", equalTo(FIRST_REQ_IDENTIFIER.description)))
            .andExpect(jsonPath("$.data[0].title", equalTo(FIRST_REQ_IDENTIFIER.title)))
            .andExpect(jsonPath("$.data[1].id", equalTo(SECOND_REQ_IDENTIFIER.id)))
            .andExpect(jsonPath("$.data[1].description", equalTo(SECOND_REQ_IDENTIFIER.description)))
            .andExpect(jsonPath("$.data[1].title", equalTo(SECOND_REQ_IDENTIFIER.title)))
            .andDo(
                document(
                    "requirements/get_all/success",
                    responseFields(ModelDescription.Requirement.collection())
                )
            )
    }

    @Test
    fun `Getting all requirements successful (no description)`() {
        initData()

        val url = getUrl()
        mockMvc.perform(
            get(url)
                .param("lang", LANGUAGE)
                .param("country", COUNTRY)
                .param("pmd", PMD)
                .param("phase", PHASE)
                .param("requirementGroupId", REQ_GROUP_NO_DESCRIPTION)
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType("application/json;charset=UTF-8"))
            .andExpect(jsonPath("$.data[0].id", equalTo(THIRD_REQ_IDENTIFIER.id)))
            .andExpect(jsonPath("$.data[0].title", equalTo(THIRD_REQ_IDENTIFIER.title)))
            .andDo(
                document(
                    "requirements/get_all/no_description/success",
                    responseFields(ModelDescription.Requirement.collection())
                )
            )
    }


    @Test
    fun `Getting all requirements returns empty (unknown language)`() {
        initData()

        val url = getUrl()
        mockMvc.perform(
            get(url)
                .param("lang", UNKNOWN_LANGUAGE)
                .param("country", COUNTRY)
                .param("pmd", PMD)
                .param("phase", PHASE)
                .param("requirementGroupId", REQ_GROUP)
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType("application/json;charset=UTF-8"))
            .andExpect(jsonPath("$.data.length()", equalTo(0)))
            .andDo(
                document(
                    "requirements/get_all/unknown_lang/success_empty",
                    responseFields(ModelDescription.Requirement.emptyCollection())
                )
            )
    }

    @Test
    fun `Getting all requirements returns empty (unknown country)`() {
        initData()

        val url = getUrl()
        mockMvc.perform(
            get(url)
                .param("lang", LANGUAGE)
                .param("country", UNKNOWN_COUNTRY)
                .param("pmd", PMD)
                .param("phase", PHASE)
                .param("requirementGroupId", REQ_GROUP)
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType("application/json;charset=UTF-8"))
            .andExpect(jsonPath("$.data.length()", equalTo(0)))
            .andDo(
                document(
                    "requirements/get_all/unknown_country/success_empty",
                    responseFields(ModelDescription.Requirement.emptyCollection())
                )
            )
    }

    @Test
    fun `Getting all requirements returns empty (unknown pmd)`() {
        initData()

        val url = getUrl()
        mockMvc.perform(
            get(url)
                .param("lang", LANGUAGE)
                .param("country", COUNTRY)
                .param("pmd", UNKNOWN_PMD)
                .param("phase", PHASE)
                .param("requirementGroupId", REQ_GROUP)
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType("application/json;charset=UTF-8"))
            .andExpect(jsonPath("$.data.length()", equalTo(0)))
            .andDo(
                document(
                    "requirements/get_all/unknown_pmd/success_empty",
                    responseFields(ModelDescription.Requirement.emptyCollection())
                )
            )
    }

    @Test
    fun `Getting all requirements returns empty (unknown phase)`() {
        initData()

        val url = getUrl()
        mockMvc.perform(
            get(url)
                .param("lang", LANGUAGE)
                .param("country", COUNTRY)
                .param("pmd", PMD)
                .param("phase", UNKNOWN_PHASE)
                .param("requirementGroupId", REQ_GROUP)
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType("application/json;charset=UTF-8"))
            .andExpect(jsonPath("$.data.length()", equalTo(0)))
            .andDo(
                document(
                    "requirements/get_all/unknown_phase/success_empty",
                    responseFields(ModelDescription.Requirement.emptyCollection())
                )
            )
    }

    @Test
    fun `Getting all requirements returns empty (unknown requirementGroupId)`() {
        initData()

        val url = getUrl()
        mockMvc.perform(
            get(url)
                .param("lang", LANGUAGE)
                .param("country", COUNTRY)
                .param("pmd", PMD)
                .param("phase", PHASE)
                .param("requirementGroupId", UNKNOWN_REQ_GROUP)
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType("application/json;charset=UTF-8"))
            .andExpect(jsonPath("$.data.length()", equalTo(0)))
            .andDo(
                document(
                    "requirements/get_all/unknown_requirement_group_id/success_empty",
                    responseFields(ModelDescription.Requirement.emptyCollection())
                )
            )
    }

    @Test
    fun `Getting all requirements fails (language request parameter is missing)`() {
        initData()

        val url = getUrl()
        mockMvc.perform(
            get(url)
                .param("country", COUNTRY)
                .param("pmd", PMD)
                .param("phase", PHASE)
                .param("requirementGroupId", REQ_GROUP)
        )
            .andExpect(status().isBadRequest)
            .andExpect(content().contentType("application/json;charset=UTF-8"))
            .andExpect(jsonPath("$.errors.length()", equalTo(1)))
            .andExpect(jsonPath("$.errors[0].code", equalTo(LANGUAGE_REQUEST_PARAMETER_MISSING.code)))
            .andExpect(
                jsonPath(
                    "$.errors[0].description",
                    equalTo("The request is missing a required query parameter - 'lang'.")
                )
            )
            .andDo(
                document(
                    "requirements/get_all/errors/no_lang_query_param",
                    responseFields(ModelDescription.responseError())
                )
            )
    }

    @Test
    fun `Getting all requirements fails (country request parameter is missing)`() {
        initData()

        val url = getUrl()
        mockMvc.perform(
            get(url)
                .param("lang", LANGUAGE)
                .param("pmd", PMD)
                .param("phase", PHASE)
                .param("requirementGroupId", REQ_GROUP)
        )
            .andExpect(status().isBadRequest)
            .andExpect(content().contentType("application/json;charset=UTF-8"))
            .andExpect(jsonPath("$.errors.length()", equalTo(1)))
            .andExpect(jsonPath("$.errors[0].code", equalTo(ErrorCode.COUNTRY_REQUEST_PARAMETER_MISSING.code)))
            .andExpect(
                jsonPath(
                    "$.errors[0].description",
                    equalTo("The request is missing a required query parameter - 'country'.")
                )
            )
            .andDo(
                document(
                    "requirements/get_all/errors/no_country_query_param",
                    responseFields(ModelDescription.responseError())
                )
            )
    }

    @Test
    fun `Getting all requirements fails (pmd request parameter is missing)`() {
        initData()

        val url = getUrl()
        mockMvc.perform(
            get(url)
                .param("lang", LANGUAGE)
                .param("country", COUNTRY)
                .param("phase", PHASE)
                .param("requirementGroupId", REQ_GROUP)
        )
            .andExpect(status().isBadRequest)
            .andExpect(content().contentType("application/json;charset=UTF-8"))
            .andExpect(jsonPath("$.errors.length()", equalTo(1)))
            .andExpect(jsonPath("$.errors[0].code", equalTo(ErrorCode.PMD_REQUEST_PARAMETER_MISSING.code)))
            .andExpect(
                jsonPath(
                    "$.errors[0].description",
                    equalTo("The request is missing a required query parameter - 'pmd'.")
                )
            )
            .andDo(
                document(
                    "requirements/get_all/errors/no_pmd_query_param",
                    responseFields(ModelDescription.responseError())
                )
            )
    }

    @Test
    fun `Getting all requirements fails (phase request parameter is missing)`() {
        initData()

        val url = getUrl()
        mockMvc.perform(
            get(url)
                .param("lang", LANGUAGE)
                .param("country", COUNTRY)
                .param("pmd", PMD)
                .param("requirementGroupId", REQ_GROUP)
        )
            .andExpect(status().isBadRequest)
            .andExpect(content().contentType("application/json;charset=UTF-8"))
            .andExpect(jsonPath("$.errors.length()", equalTo(1)))
            .andExpect(jsonPath("$.errors[0].code", equalTo(ErrorCode.PHASE_REQUEST_PARAMETER_MISSING.code)))
            .andExpect(
                jsonPath(
                    "$.errors[0].description",
                    equalTo("The request is missing a required query parameter - 'phase'.")
                )
            )
            .andDo(
                document(
                    "requirements/get_all/errors/no_phase_query_param",
                    responseFields(ModelDescription.responseError())
                )
            )
    }

    @Test
    fun `Getting all requirements fails (requirementGroupId request parameter is missing)`() {
        initData()

        val url = getUrl()
        mockMvc.perform(
            get(url)
                .param("lang", LANGUAGE)
                .param("country", COUNTRY)
                .param("pmd", PMD)
                .param("phase", PHASE)
        )
            .andExpect(status().isBadRequest)
            .andExpect(content().contentType("application/json;charset=UTF-8"))
            .andExpect(jsonPath("$.errors.length()", equalTo(1)))
            .andExpect(jsonPath("$.errors[0].code", equalTo(ErrorCode.REQUIREMENT_GROUP_REQUEST_PARAMETER_MISSING.code)))
            .andExpect(
                jsonPath(
                    "$.errors[0].description",
                    equalTo("The request is missing a required query parameter - 'requirementGroupId'.")
                )
            )
            .andDo(
                document(
                    "requirements/get_all/errors/no_requirement_group_id_query_param",
                    responseFields(ModelDescription.responseError())
                )
            )
    }

    @Test
    fun `Getting all requirements is error (language request parameter is empty)`() {
        initData()

        val url = getUrl()
        mockMvc.perform(
            get(url)
                .param("lang", EMPTY_PARAMETER)
                .param("country", COUNTRY)
                .param("pmd", PMD)
                .param("phase", PHASE)
                .param("requirementGroupId", REQ_GROUP)
        )
            .andExpect(status().isBadRequest)
            .andExpect(content().contentType("application/json;charset=UTF-8"))
            .andExpect(jsonPath("$.errors.length()", equalTo(1)))
            .andExpect(jsonPath("$.errors[0].code", equalTo(ErrorCode.INVALID_LANGUAGE_CODE.code)))
            .andExpect(
                jsonPath(
                    "$.errors[0].description",
                    equalTo("Invalid language code (value is blank).")
                )
            )
            .andDo(
                document(
                    "requirements/get_all/errors/empty_lang",
                    responseFields(ModelDescription.responseError())
                )
            )
    }

    @Test
    fun `Getting all requirements is error (country request parameter is empty)`() {
        initData()

        val url = getUrl()
        mockMvc.perform(
            get(url)
                .param("lang", LANGUAGE)
                .param("country", EMPTY_PARAMETER)
                .param("pmd", PMD)
                .param("phase", PHASE)
                .param("requirementGroupId", REQ_GROUP)
        )
            .andExpect(status().isBadRequest)
            .andExpect(content().contentType("application/json;charset=UTF-8"))
            .andExpect(jsonPath("$.errors.length()", equalTo(1)))
            .andExpect(jsonPath("$.errors[0].code", equalTo(ErrorCode.INVALID_COUNTRY_CODE.code)))
            .andExpect(
                jsonPath(
                    "$.errors[0].description",
                    equalTo("Invalid country code (value is blank).")
                )
            )
            .andDo(
                document(
                    "requirements/get_all/errors/empty_country",
                    responseFields(ModelDescription.responseError())
                )
            )
    }

    @Test
    fun `Getting all requirements is error (pmd request parameter is empty)`() {
        initData()

        val url = getUrl()
        mockMvc.perform(
            get(url)
                .param("lang", LANGUAGE)
                .param("country", COUNTRY)
                .param("pmd", EMPTY_PARAMETER)
                .param("phase", PHASE)
                .param("requirementGroupId", REQ_GROUP)
        )
            .andExpect(status().isBadRequest)
            .andExpect(content().contentType("application/json;charset=UTF-8"))
            .andExpect(jsonPath("$.errors.length()", equalTo(1)))
            .andExpect(jsonPath("$.errors[0].code", equalTo(ErrorCode.INVALID_PMD.code)))
            .andExpect(
                jsonPath(
                    "$.errors[0].description",
                    equalTo("Invalid pmd (value is blank).")
                )
            )
            .andDo(
                document(
                    "requirements/get_all/errors/empty_pmd",
                    responseFields(ModelDescription.responseError())
                )
            )
    }

    @Test
    fun `Getting all requirements is error (phase request parameter is empty)`() {
        initData()

        val url = getUrl()
        mockMvc.perform(
            get(url)
                .param("lang", LANGUAGE)
                .param("country", COUNTRY)
                .param("pmd", PMD)
                .param("phase", EMPTY_PARAMETER)
                .param("requirementGroupId", REQ_GROUP)
        )
            .andExpect(status().isBadRequest)
            .andExpect(content().contentType("application/json;charset=UTF-8"))
            .andExpect(jsonPath("$.errors.length()", equalTo(1)))
            .andExpect(jsonPath("$.errors[0].code", equalTo(ErrorCode.INVALID_PHASE.code)))
            .andExpect(
                jsonPath(
                    "$.errors[0].description",
                    equalTo("Invalid phase (value is blank).")
                )
            )
            .andDo(
                document(
                    "requirements/get_all/errors/empty_phase",
                    responseFields(ModelDescription.responseError())
                )
            )
    }

    @Test
    fun `Getting all requirements is error (requirementGroupId request parameter is empty)`() {
        initData()

        val url = getUrl()
        mockMvc.perform(
            get(url)
                .param("lang", LANGUAGE)
                .param("country", COUNTRY)
                .param("pmd", PMD)
                .param("phase", PHASE)
                .param("requirementGroupId", EMPTY_PARAMETER)
        )
            .andExpect(status().isBadRequest)
            .andExpect(content().contentType("application/json;charset=UTF-8"))
            .andExpect(jsonPath("$.errors.length()", equalTo(1)))
            .andExpect(jsonPath("$.errors[0].code", equalTo(ErrorCode.INVALID_REQUIREMENT_GROUP.code)))
            .andExpect(
                jsonPath(
                    "$.errors[0].description",
                    equalTo("Invalid requirement group code (value is blank).")
                )
            )
            .andDo(
                document(
                    "requirements/get_all/errors/empty_requirement_group_id",
                    responseFields(ModelDescription.responseError())
                )
            )
    }

    private fun initData() {
        val sqlCriterion = loadSql("sql/requirement/requirements_init_data.sql")
        executeSQLScript(sqlCriterion)
    }

    private fun getUrl(): String = "/requirements"
}
