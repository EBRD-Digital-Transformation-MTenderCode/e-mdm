package com.procurement.mdm.repository;

//import com.procurement.mdm.model.entity.CPV;
//import com.procurement.mdm.model.entity.Language;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.BDDMockito.given;
//import static org.mockito.Mockito.mock;
//
//@ExtendWith(SpringExtension.class)
class CpvRepositoryTest {
//
//    private static CPVRepository cpvRepository;
//
//    private static CPV cpv;
//
//    private static Language language;
//
//    @BeforeAll
//    static void setUp() {
//        List<CPV> cpvs = new ArrayList<>();
//        language = new Language();
//        language.setIso6391("en");
//        cpv = new CPV();
//        cpv.setCode("03000000-1");
//        cpv.setName("Test cpv");
//        cpv.setGroup(1);
//        cpv.setParent("03000000-1");
//        cpv.setLanguage(language);
//        cpvs.add(cpv);
//        cpvRepository = mock(CPVRepository.class);
//        given(cpvRepository.findCpvsByLanguage_Iso6391(language.getIso6391())).willReturn(cpvs);
//        given(cpvRepository.findCpvsByLanguage_Iso6391AndGroup(language.getIso6391(), cpv.getGroup())).willReturn(cpvs);
//        given(cpvRepository.findCpvsByLanguage_Iso6391AndParent(language.getIso6391(), cpv.getParent())).willReturn(cpvs);
//        given(cpvRepository.findCpvsByLanguage_Iso6391AndGroupAndParent(language.getIso6391(), cpv.getGroup(), cpv.getParent())).willReturn(cpvs);
//    }
//
//    @Test
//    void findCpvsByLanguage_Id() {
//        List<CPV> cpvs = cpvRepository.findCpvsByLanguage_Iso6391(language.getIso6391());
//        assertTrue(cpvs.get(0).getCode().equals(cpv.getCode()));
//    }
//
//    @Test
//    void findCpvsByLanguage_IdAndGroupAndParent() {
//        List<CPV> cpvs = cpvRepository.findCpvsByLanguage_Iso6391AndGroupAndParent(language.getIso6391(), cpv.getGroup(), cpv
//            .getParent());
//        assertTrue(cpvs.get(0).getCode().equals(cpv.getCode()));
//    }
//
//    @Test
//    void findCpvsByLanguage_IdAndGroup() {
//        List<CPV> cpvs = cpvRepository.findCpvsByLanguage_Iso6391AndGroup(language.getIso6391(), cpv.getGroup());
//        assertTrue(cpvs.get(0).getCode().equals(cpv.getCode()));
//    }
//
//    @Test
//    void findCpvsByLanguage_IdAndParent() {
//        List<CPV> cpvs = cpvRepository.findCpvsByLanguage_Iso6391AndParent(language.getIso6391(), cpv.getParent());
//        assertTrue(cpvs.get(0).getCode().equals(cpv.getCode()));
//    }
}