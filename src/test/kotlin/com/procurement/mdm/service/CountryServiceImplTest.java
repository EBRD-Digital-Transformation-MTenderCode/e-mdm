package com.procurement.mdm.service;

//import com.procurement.mdm.model.entity.Country;
//import com.procurement.mdm.model.entity.Language;
//import com.procurement.mdm.repository.CountryRepository;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertTrue;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(SpringExtension.class)
class CountryServiceImplTest {
//
//    private static CountryService countryService;
//
//    private static Country country;
//
//    @BeforeAll
//    static void setUp() {
//        final List<Country> countries = new ArrayList<>();
//        Language language = new Language();
//        language.setIso6391("en");
//        country = new Country();
//        country.setCode("0-000001");
//        country.setName("Test country");
//        country.setDescription("Test description");
//        country.setPhoneCode("+380991112233");
//        country.setLanguage(language);
//        countries.add(country);
//        CountryRepository countryRepository = mock(CountryRepository.class);
//        when(countryRepository.findAll()).thenReturn(countries);
//        when(countryRepository.findCountriesByCode(country.getCode())).thenReturn(countries);
//        when(countryRepository.findCountriesByName(country.getName())).thenReturn(countries);
//        countryService = new CountryServiceImpl(countryRepository);
//    }
//
//    @Test
//    void getAllCountries() {
//        List<Country> countries = countryService.getAllCountries();
//        assertTrue(countries.get(0).getCode().equals(country.getCode()));
//    }
//
//    @Test
//    void getCountriesByCode() {
//        List<Country> countries = countryService.getCountriesByCode(country.getCode());
//        assertTrue(countries.get(0).getCode().equals(country.getCode()));
//    }
//
//    @Test
//    void getCountriesByName() {
//        List<Country> countries = countryService.getCountriesByName(country.getName());
//        assertTrue(countries.get(0).getName().equals(country.getName()));
//    }
}

