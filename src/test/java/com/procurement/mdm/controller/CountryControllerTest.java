package com.procurement.mdm.controller;

import com.procurement.mdm.model.entity.Country;
import com.procurement.mdm.service.CountryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(CountryController.class)
class CountryControllerTest {


    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private CountryService countryService;

    private Country country;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        List<Country> countries = new ArrayList<>();
        country = new Country();
        country.setId(1L);
        country.setCode("0-000001");
        country.setLanguageId(41L);
        country.setName("Test country");
        country.setDescription("Test description");
        country.setPhoneCode("+380991112233");
        countries.add(country);
        given(this.countryService.getAllCountries()).willReturn(countries);
        given(this.countryService.getCountriesById(country.getId())).willReturn(countries);
        given(this.countryService.getCountriesByName(country.getName())).willReturn(countries);
        given(this.countryService.getCountriesByCode(country.getCode())).willReturn(countries);
    }

    @Test
    void getCountries() throws Exception{
        mockMvc.perform(get("/countries")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].code", is(country.getCode())))
            .andReturn();
    }

    @Test
    void getCountriesById() throws Exception{
        mockMvc.perform(get("/countries/"+String.valueOf(country.getId()))
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].code", is(country.getCode())))
            .andReturn();
    }

    @Test
    void getCountriesByName() throws Exception{
        mockMvc.perform(get("/countries/byName")
            .param("name", country.getName())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].code", is(country.getCode())))
            .andReturn();
    }

    @Test
    void getCountriesByCode() throws Exception{
        mockMvc.perform(get("/countries/byCode")
            .param("code", country.getCode())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].code", is(country.getCode())))
            .andReturn();
    }
}