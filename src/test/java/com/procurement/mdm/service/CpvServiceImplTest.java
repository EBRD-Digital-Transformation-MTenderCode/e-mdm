package com.procurement.mdm.service;

import com.procurement.mdm.model.entity.Cpv;
import com.procurement.mdm.model.entity.Language;
import com.procurement.mdm.repositories.CountryRepository;
import com.procurement.mdm.repositories.CpvRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(SpringExtension.class)
class CpvServiceImplTest {

    private CpvServiceImpl cpvService;

    @MockBean
    private CpvRepository cpvRepository;

    private Cpv cpv;
    private Language language;

    @BeforeEach
    void setUp() {
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
        given(this.cpvRepository.findCpvsByLanguage_Id(language.getId())).willReturn(cpvs);
        given(this.cpvRepository.findCpvsByLanguage_IdAndGroup(language.getId(), cpv.getGroup())).willReturn(cpvs);
        given(this.cpvRepository.findCpvsByLanguage_IdAndParent(language.getId(), cpv.getParent())).willReturn(cpvs);
        given(this.cpvRepository.findCpvsByLanguage_IdAndGroupAndParent(language.getId(), cpv.getGroup(), cpv.getParent())).willReturn(cpvs);
        this.cpvService = new CpvServiceImpl(cpvRepository);
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
    void getCpvByParam() {
        List<Cpv> cpvs = cpvService.getCpvByParam(null, null, null);
        assertTrue(cpvs.isEmpty());
        List<Cpv> cpvs1 = cpvService.getCpvByParam(language.getId(), null, null);
        assertTrue(cpvs1.get(0).getCode().equals(cpv.getCode()));
        List<Cpv> cpvs2 = cpvService.getCpvByParam(language.getId(), null, cpv.getParent());
        assertTrue(cpvs2.get(0).getCode().equals(cpv.getCode()));
        List<Cpv> cpvs3 = cpvService.getCpvByParam(language.getId(), cpv.getGroup(), null);
        assertTrue(cpvs3.get(0).getCode().equals(cpv.getCode()));
        List<Cpv> cpvs4 = cpvService.getCpvByParam(language.getId(), cpv.getGroup(), cpv.getParent());
        assertTrue(cpvs4.get(0).getCode().equals(cpv.getCode()));
    }
}