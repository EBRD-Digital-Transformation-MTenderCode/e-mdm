package com.procurement.mdm.controller;

import com.procurement.mdm.model.entity.Cpv;
import com.procurement.mdm.model.entity.Language;
import com.procurement.mdm.service.CpvService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
class CpvControllerTest {

    private static MockMvc mockMvc;

    private static Cpv cpv;
    private static Language language;

    @BeforeAll
    static void setUp() {
        final CpvService cpvService = mock(CpvService.class);
        final CpvController cpvController = new CpvController(cpvService);
        final List<Cpv> cpvs = new ArrayList<>();
        mockMvc = MockMvcBuilders.standaloneSetup(cpvController).build();
        language = new Language();
        language.setIso6391("en");
        cpv = new Cpv();
        cpv.setCode("03000000-1");
        cpv.setName("Test cpv");
        cpv.setGroup(1);
        cpv.setParent("03000000-1");
        cpv.setLanguage(language);
        cpvs.add(cpv);
        when(cpvService.getCpvByLanguageCode(language.getIso6391())).thenReturn(cpvs);
        when(cpvService.getCpvByParam(language.getIso6391(), cpv.getGroup(), cpv.getParent())).thenReturn(cpvs);
    }

    @Test
    void getCpvByLanguageCode() throws Exception {
        mockMvc.perform(get("/cpv/byLanguage")
            .param("languageCode", String.valueOf(cpv.getLanguage().getIso6391()))
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].code", is(cpv.getCode())))
            .andReturn();
    }

    @Test
    void getCpvByParam() throws Exception {
        mockMvc.perform(get("/cpv/byParam")
            .param("languageCode", String.valueOf(cpv.getLanguage().getIso6391()))
            .param("group", String.valueOf(cpv.getGroup()))
            .param("parent", cpv.getParent())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].code", is(cpv.getCode())))
            .andReturn();
    }
}