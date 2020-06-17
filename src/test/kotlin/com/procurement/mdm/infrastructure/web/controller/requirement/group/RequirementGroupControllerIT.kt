package com.procurement.mdm.infrastructure.web.controller.requirement.group

import com.procurement.mdm.application.service.requirement.group.RequirementGroupService
import com.procurement.mdm.application.service.requirement.group.RequirementGroupServiceImpl
import com.procurement.mdm.domain.model.identifier.RequirementGroupIdentifier
import com.procurement.mdm.domain.repository.requirement.group.RequirementGroupRepository
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
class RequirementGroupControllerIT : AbstractRepositoryTest() {
    companion object {
        private const val LANGUAGE = "en"
        private const val UNKNOWN_LANGUAGE = "fr"

        private const val COUNTRY = "md"
        private const val UNKNOWN_COUNTRY = "uk"

        private const val PMD = "ot"
        private const val UNKNOWN_PMD = "gpa"

        private const val PHASE = "submission"
        private val UNKNOWN_PHASE = "awarding"

        private const val CRITERION = "md_ot_1"
        private const val UNKNOWN_CRITERION = "md_gpa_1"

        private const val CRITERION_NO_DESCRIPTION = "md_ot_3"
        private const val REQ_GROUP_CODE_NO_DESCRIPTION = "REQ_GROUP_3"

        private const val EMPTY_PARAMETER = "  "

        private val FIRST_REQ_GROUP_IDENTIFIER = RequirementGroupIdentifier(
            id = "REQ_GROUP_1",
            description = "req-group-description-1"
        )
        private val SECOND_REQ_GROUP_IDENTIFIER = RequirementGroupIdentifier(
            id = "REQ_GROUP_1_1",
            description = "req-group-description-1-1"
        )
    }

    private lateinit var mockMvc: MockMvc
    private lateinit var requirementGroupService: RequirementGroupService

    @Autowired
    private lateinit var requirementGroupRepository: RequirementGroupRepository

    @BeforeEach
    fun init(restDocumentation: RestDocumentationContextProvider) {
        requirementGroupService = RequirementGroupServiceImpl(requirementGroupRepository)

        val controller = RequirementGroupController(requirementGroupService)
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
    fun `Getting all requirement groups is successful`() {
        initData()

        val url = getUrl()
        mockMvc.perform(
            get(url)
                .param("lang", LANGUAGE)
                .param("country", COUNTRY)
                .param("pmd", PMD)
                .param("phase", PHASE)
                .param("criterionId", CRITERION)
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType("application/json;charset=UTF-8"))
            .andExpect(jsonPath("$.data.length()", equalTo(2)))
            .andExpect(jsonPath("$.data[0].id", equalTo(FIRST_REQ_GROUP_IDENTIFIER.id)))
            .andExpect(jsonPath("$.data[0].description", equalTo(FIRST_REQ_GROUP_IDENTIFIER.description)))
            .andExpect(jsonPath("$.data[1].id", equalTo(SECOND_REQ_GROUP_IDENTIFIER.id)))
            .andExpect(jsonPath("$.data[1].description", equalTo(SECOND_REQ_GROUP_IDENTIFIER.description)))
            .andDo(
                document(
                    "requirementGroups/get_all/success",
                    responseFields(ModelDescription.RequirementGroup.collection())
                )
            )
    }

    @Test
    fun `Getting all requirement groups successful (no description)`() {
        initData()

        val url = getUrl()
        mockMvc.perform(
            get(url)
                .param("lang", UNKNOWN_LANGUAGE)
                .param("country", COUNTRY)
                .param("pmd", PMD)
                .param("phase", PHASE)
                .param("criterionId", CRITERION_NO_DESCRIPTION)
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType("application/json;charset=UTF-8"))
            .andExpect(jsonPath("$.data[0].id", equalTo(REQ_GROUP_CODE_NO_DESCRIPTION)))
            .andDo(
                document(
                    "requirementGroups/get_all/no_description/success",
                    responseFields(ModelDescription.RequirementGroup.collection())
                )
            )
    }

    @Test
    fun `Getting all requirement groups returns empty (unknown language)`() {
        initData()

        val url = getUrl()
        mockMvc.perform(
            get(url)
                .param("lang", UNKNOWN_LANGUAGE)
                .param("country", COUNTRY)
                .param("pmd", PMD)
                .param("phase", PHASE)
                .param("criterionId", CRITERION)
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType("application/json;charset=UTF-8"))
            .andExpect(jsonPath("$.data.length()", equalTo(0)))
            .andDo(
                document(
                    "requirementGroups/get_all/unknown_lang/success_empty",
                    responseFields(ModelDescription.RequirementGroup.emptyCollection())
                )
            )
    }

    @Test
    fun `Getting all requirement groups returns empty (unknown country)`() {
        initData()

        val url = getUrl()
        mockMvc.perform(
            get(url)
                .param("lang", LANGUAGE)
                .param("country", UNKNOWN_COUNTRY)
                .param("pmd", PMD)
                .param("phase", PHASE)
                .param("criterionId", CRITERION)
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType("application/json;charset=UTF-8"))
            .andExpect(jsonPath("$.data.length()", equalTo(0)))
            .andDo(
                document(
                    "requirementGroups/get_all/unknown_country/success_empty",
                    responseFields(ModelDescription.RequirementGroup.emptyCollection())
                )
            )
    }

    @Test
    fun `Getting all requirement groups returns empty (unknown pmd)`() {
        initData()

        val url = getUrl()
        mockMvc.perform(
            get(url)
                .param("lang", LANGUAGE)
                .param("country", COUNTRY)
                .param("pmd", UNKNOWN_PMD)
                .param("phase", PHASE)
                .param("criterionId", CRITERION)
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType("application/json;charset=UTF-8"))
            .andExpect(jsonPath("$.data.length()", equalTo(0)))
            .andDo(
                document(
                    "requirementGroups/get_all/unknown_pmd/success_empty",
                    responseFields(ModelDescription.RequirementGroup.emptyCollection())
                )
            )
    }

    @Test
    fun `Getting all requirement groups returns empty (unknown phase)`() {
        initData()

        val url = getUrl()
        mockMvc.perform(
            get(url)
                .param("lang", LANGUAGE)
                .param("country", COUNTRY)
                .param("pmd", PMD)
                .param("phase", UNKNOWN_PHASE)
                .param("criterionId", CRITERION)
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType("application/json;charset=UTF-8"))
            .andExpect(jsonPath("$.data.length()", equalTo(0)))
            .andDo(
                document(
                    "requirementGroups/get_all/unknown_phase/success_empty",
                    responseFields(ModelDescription.RequirementGroup.emptyCollection())
                )
            )
    }

    @Test
    fun `Getting all requirement groups returns empty (unknown criterionId)`() {
        initData()

        val url = getUrl()
        mockMvc.perform(
            get(url)
                .param("lang", LANGUAGE)
                .param("country", COUNTRY)
                .param("pmd", PMD)
                .param("phase", PHASE)
                .param("criterionId", UNKNOWN_CRITERION)
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType("application/json;charset=UTF-8"))
            .andExpect(jsonPath("$.data.length()", equalTo(0)))
            .andDo(
                document(
                    "requirementGroups/get_all/unknown_phase/success_empty",
                    responseFields(ModelDescription.RequirementGroup.emptyCollection())
                )
            )
    }

    @Test
    fun `Getting all requirement groups fails (language request parameter is missing)`() {
        initData()

        val url = getUrl()
        mockMvc.perform(
            get(url)
                .param("country", COUNTRY)
                .param("pmd", PMD)
                .param("phase", PHASE)
                .param("criterionId", CRITERION)
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
                    "requirementGroups/get_all/errors/no_lang_query_param",
                    responseFields(ModelDescription.responseError())
                )
            )
    }

    @Test
    fun `Getting all requirement groups fails (country request parameter is missing)`() {
        initData()

        val url = getUrl()
        mockMvc.perform(
            get(url)
                .param("lang", LANGUAGE)
                .param("pmd", PMD)
                .param("phase", PHASE)
                .param("criterionId", CRITERION)
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
                    "requirementGroups/get_all/errors/no_country_query_param",
                    responseFields(ModelDescription.responseError())
                )
            )
    }

    @Test
    fun `Getting all requirement groups fails (pmd request parameter is missing)`() {
        initData()

        val url = getUrl()
        mockMvc.perform(
            get(url)
                .param("lang", LANGUAGE)
                .param("country", COUNTRY)
                .param("phase", PHASE)
                .param("criterionId", CRITERION)
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
                    "requirementGroups/get_all/errors/no_pmd_query_param",
                    responseFields(ModelDescription.responseError())
                )
            )
    }

    @Test
    fun `Getting all requirement groups fails (phase request parameter is missing)`() {
        initData()

        val url = getUrl()
        mockMvc.perform(
            get(url)
                .param("lang", LANGUAGE)
                .param("country", COUNTRY)
                .param("pmd", PMD)
                .param("criterionId", CRITERION)
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
                    "requirementGroups/get_all/errors/no_phase_query_param",
                    responseFields(ModelDescription.responseError())
                )
            )
    }

    @Test
    fun `Getting all requirement groups fails (criterionId request parameter is missing)`() {
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
            .andExpect(jsonPath("$.errors[0].code", equalTo(ErrorCode.CRITERION_REQUEST_PARAMETER_MISSING.code)))
            .andExpect(
                jsonPath(
                    "$.errors[0].description",
                    equalTo("The request is missing a required query parameter - 'criterionId'.")
                )
            )
            .andDo(
                document(
                    "requirementGroups/get_all/errors/no_criterion_id_query_param",
                    responseFields(ModelDescription.responseError())
                )
            )
    }

    @Test
    fun `Getting all requirement groups is error (language request parameter is empty)`() {
        initData()

        val url = getUrl()
        mockMvc.perform(
            get(url)
                .param("lang", EMPTY_PARAMETER)
                .param("country", COUNTRY)
                .param("pmd", PMD)
                .param("phase", PHASE)
                .param("criterionId", CRITERION)
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
                    "requirementGroups/get_all/errors/empty_lang",
                    responseFields(ModelDescription.responseError())
                )
            )
    }

    @Test
    fun `Getting all requirement groups is error (country request parameter is empty)`() {
        initData()

        val url = getUrl()
        mockMvc.perform(
            get(url)
                .param("lang", LANGUAGE)
                .param("country", EMPTY_PARAMETER)
                .param("pmd", PMD)
                .param("phase", PHASE)
                .param("criterionId", CRITERION)
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
                    "requirementGroups/get_all/errors/empty_country",
                    responseFields(ModelDescription.responseError())
                )
            )
    }

    @Test
    fun `Getting all requirement groups is error (pmd request parameter is empty)`() {
        initData()

        val url = getUrl()
        mockMvc.perform(
            get(url)
                .param("lang", LANGUAGE)
                .param("country", COUNTRY)
                .param("pmd", EMPTY_PARAMETER)
                .param("phase", PHASE)
                .param("criterionId", CRITERION)
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
                    "requirementGroups/get_all/errors/empty_pmd",
                    responseFields(ModelDescription.responseError())
                )
            )
    }

    @Test
    fun `Getting all requirement groups is error (phase request parameter is empty)`() {
        initData()

        val url = getUrl()
        mockMvc.perform(
            get(url)
                .param("lang", LANGUAGE)
                .param("country", COUNTRY)
                .param("pmd", PMD)
                .param("phase", EMPTY_PARAMETER)
                .param("criterionId", CRITERION)
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
                    "requirementGroups/get_all/errors/empty_phase",
                    responseFields(ModelDescription.responseError())
                )
            )
    }

    @Test
    fun `Getting all requirement groups is error (criterionId request parameter is empty)`() {
        initData()

        val url = getUrl()
        mockMvc.perform(
            get(url)
                .param("lang", LANGUAGE)
                .param("country", COUNTRY)
                .param("pmd", PMD)
                .param("phase", PHASE)
                .param("criterionId", EMPTY_PARAMETER)
        )
            .andExpect(status().isBadRequest)
            .andExpect(content().contentType("application/json;charset=UTF-8"))
            .andExpect(jsonPath("$.errors.length()", equalTo(1)))
            .andExpect(jsonPath("$.errors[0].code", equalTo(ErrorCode.INVALID_CRITERION.code)))
            .andExpect(
                jsonPath(
                    "$.errors[0].description",
                    equalTo("Invalid criteria code (value is blank).")
                )
            )
            .andDo(
                document(
                    "requirementGroups/get_all/errors/empty_criterion",
                    responseFields(ModelDescription.responseError())
                )
            )
    }

    private fun initData() {
        val sqlCriterion = loadSql("sql/requirement.group/requirement_groups_init_data.sql")
        executeSQLScript(sqlCriterion)
    }

    private fun getUrl(): String = "/requirementGroups"
}
