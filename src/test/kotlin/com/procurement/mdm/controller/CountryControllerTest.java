package com.procurement.mdm.controller;

//import com.procurement.mdm.model.entity.Country;
//import com.procurement.mdm.model.entity.Language;
//import com.procurement.mdm.service.CountryService;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.hamcrest.Matchers.is;
//import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.data.MockMvcResultMatchers.content;
//import static org.springframework.test.web.servlet.data.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.data.MockMvcResultMatchers.status;

//@ExtendWith(SpringExtension.class)
class CountryControllerTest {
//
//    private static MockMvc mockMvc;
//
//    private static Country country;
//
//    @BeforeAll
//    static void setUp() {
//        final CountryService countryService = mock(CountryService.class);
//        final CountryController countryController = new CountryController(countryService);
//        final List<Country> countries = new ArrayList<>();
//        mockMvc = MockMvcBuilders.standaloneSetup(countryController).build();
//        Language language = new Language();
//        language.setIso6391("en");
//        country = new Country();
//        country.setCode("0-000001");
//        country.setName("Test country");
//        country.setDescription("Test description");
//        country.setPhoneCode("+380991112233");
//        country.setLanguage(language);
//        countries.add(country);
//        when(countryService.getAllCountries()).thenReturn(countries);
//        when(countryService.getCountriesByCode(country.getCode())).thenReturn(countries);
//        when(countryService.getCountriesByName(country.getName())).thenReturn(countries);
//    }
//
//    @Test
//    void getCountries() throws Exception {
//        mockMvc.perform(get("/countries")
//            .accept(MediaType.APPLICATION_JSON))
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
//            .andExpect(jsonPath("$", hasSize(1)))
//            .andExpect(jsonPath("$[0].code", is(country.getCode())))
//            .andReturn();
//    }
//
//    @Test
//    void getCountriesByCode() throws Exception {
//        mockMvc.perform(get("/countries/byCode")
//            .param("code", country.getCode())
//            .accept(MediaType.APPLICATION_JSON))
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
//            .andExpect(jsonPath("$", hasSize(1)))
//            .andExpect(jsonPath("$[0].code", is(country.getCode())))
//            .andReturn();
//    }
//
//    @Test
//    void getCountriesByName() throws Exception {
//        mockMvc.perform(get("/countries/byName")
//            .param("name", country.getName())
//            .accept(MediaType.APPLICATION_JSON))
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
//            .andExpect(jsonPath("$", hasSize(1)))
//            .andExpect(jsonPath("$[0].code", is(country.getCode())))
//            .andReturn();
//    }
}