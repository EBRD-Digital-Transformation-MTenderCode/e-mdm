package com.procurement.mdm.repository;

//import com.procurement.mdm.model.entity.Country;
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
class CountryRepositoryTest {
//
//    private static CountryRepository countryRepository;
//
//    private static Country country;
//
//    @BeforeAll
//    static void setUp() {
//        List<Country> countries = new ArrayList<>();
//        Language language = new Language();
//        language.setIso6391("en");
//        country = new Country();
//        country.setCode("0-000001");
//        country.setName("Test country");
//        country.setDescription("Test description");
//        country.setPhoneCode("+380991112233");
//        country.setLanguage(language);
//        countries.add(country);
//        countryRepository = mock(CountryRepository.class);
//        given(countryRepository.findCountriesByName(country.getName())).willReturn(countries);
//        given(countryRepository.findCountriesByCode(country.getCode())).willReturn(countries);
//    }
//
//    @Test
//    void findCountriesByCode() {
//        List<Country> countries = countryRepository.findCountriesByCode(country.getCode());
//        assertTrue(countries.get(0).getCode().equals(country.getCode()));
//    }
//
//    @Test
//    void findCountriesByName() {
//        List<Country> countries = countryRepository.findCountriesByName(country.getName());
//        assertTrue(countries.get(0).getName().equals(country.getName()));
//    }
}