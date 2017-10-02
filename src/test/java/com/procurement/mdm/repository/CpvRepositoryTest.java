package com.procurement.mdm.repository;

import com.procurement.mdm.model.entity.Cpv;
import com.procurement.mdm.model.entity.Language;
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
class CpvRepositoryTest {

    @MockBean
    private CpvRepository cpvRepository;

    private Cpv cpv;
    private Language language;

    @BeforeEach
    void setUp() {
        List<Cpv> cpvs = new ArrayList<>();
        language = new Language();
        language.setId(41L);
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
    }

    @Test
    void findCpvsByLanguage_Id() {
        List<Cpv> cpvs = cpvRepository.findCpvsByLanguage_Id(language.getId());
        assertTrue(cpvs.get(0).getCode().equals(cpv.getCode()));
    }

    @Test
    void findCpvsByLanguage_IdAndGroupAndParent() {
        List<Cpv> cpvs = cpvRepository.findCpvsByLanguage_IdAndGroupAndParent(language.getId(), cpv.getGroup(), cpv
            .getParent());
        assertTrue(cpvs.get(0).getCode().equals(cpv.getCode()));
    }

    @Test
    void findCpvsByLanguage_IdAndGroup() {
        List<Cpv> cpvs = cpvRepository.findCpvsByLanguage_IdAndGroup(language.getId(), cpv.getGroup());
        assertTrue(cpvs.get(0).getCode().equals(cpv.getCode()));
    }

    @Test
    void findCpvsByLanguage_IdAndParent() {
        List<Cpv> cpvs = cpvRepository.findCpvsByLanguage_IdAndParent(language.getId(), cpv.getParent());
        assertTrue(cpvs.get(0).getCode().equals(cpv.getCode()));
    }
}