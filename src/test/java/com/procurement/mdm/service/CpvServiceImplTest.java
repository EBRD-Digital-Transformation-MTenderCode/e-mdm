package com.procurement.mdm.service;

import com.procurement.mdm.model.entity.Cpv;
import com.procurement.mdm.model.entity.Language;
import com.procurement.mdm.repository.CpvRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class CpvServiceImplTest {

    private static CpvService cpvService;

    private static Cpv cpv;

    private static Language language;

    @BeforeAll
    static void setUp() {
        List<Cpv> cpvs = new ArrayList<>();
        language = new Language();
        language.setId(41L);
        language.setSNo(41);
        language.setName("Indo-European");
        language.setFamily("English");
        language.setIso6391("en");
        language.setDescription("test language");
        cpv = new Cpv();
        cpv.setCode("03000000-1");
        cpv.setName("Test cpv");
        cpv.setGroup(1);
        cpv.setParent("03000000-1");
        cpv.setLanguage(language);
        cpvs.add(cpv);
        CpvRepository cpvRepository = mock(CpvRepository.class);
        when(cpvRepository.findCpvsByLanguage_Id(language.getId())).thenReturn(cpvs);
        when(cpvRepository.findCpvsByLanguage_IdAndGroup(language.getId(), cpv.getGroup())).thenReturn(cpvs);
        when(cpvRepository.findCpvsByLanguage_IdAndParent(language.getId(), cpv.getParent())).thenReturn(cpvs);
        when(cpvRepository.findCpvsByLanguage_IdAndGroupAndParent(language.getId(), cpv.getGroup(), cpv.getParent()))
            .thenReturn(cpvs);
        cpvService = new CpvServiceImpl(cpvRepository);
    }

    @Test
    void getLanguage() {
        assertTrue(language.getId().equals(41L));
        assertTrue(language.getSNo().equals(41));
        assertTrue(language.getName().equals("Indo-European"));
        assertTrue(language.getFamily().equals("English"));
        assertTrue(language.getIso6391().equals("en"));
        assertTrue(language.getDescription().equals("test language"));
    }

    @Test
    void getCpvByLanguage() {
        List<Cpv> cpvs = cpvService.getCpvByLanguage(cpv.getLanguage().getId());
        assertTrue(cpvs.get(0).getCode().equals(cpv.getCode()));
    }

    @Test
    void getCpvByParamLanguageId() {
        List<Cpv> cpvs = cpvService.getCpvByParam(language.getId());
        assertTrue(cpvs.get(0).getCode().equals(cpv.getCode()));
    }

    @Test
    void getCpvByParamsLanguageIdAndGroup() {
        List<Cpv> cpvs = cpvService.getCpvByParam(language.getId(), cpv.getGroup());
        assertTrue(cpvs.get(0).getCode().equals(cpv.getCode()));
    }

    @Test
    void getCpvByParamsLanguageIdAndParent() {
        List<Cpv> cpvs = cpvService.getCpvByParam(language.getId(), cpv.getParent());
        assertTrue(cpvs.get(0).getCode().equals(cpv.getCode()));
    }

    @Test
    void getCpvByParamsLanguageIdAndGroupAndParent() {
        List<Cpv> cpvs = cpvService.getCpvByParam(language.getId(), cpv.getGroup(), cpv.getParent());
        assertTrue(cpvs.get(0).getCode().equals(cpv.getCode()));
    }
}