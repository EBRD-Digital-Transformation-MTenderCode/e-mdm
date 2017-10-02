package com.procurement.mdm.service;

import com.procurement.mdm.model.entity.Country;
import com.procurement.mdm.repositories.CountryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(SpringExtension.class)
class CountryServiceImplTest {


    private CountryServiceImpl countryService;

    @MockBean
    private CountryRepository countryRepository;

    private Country country;

    @BeforeEach
    void setUp() {
        List<Country> countries = new ArrayList<>();
        country = new Country();
        country.setId(1L);
        country.setCode("0-000001");
        country.setLanguageId(41L);
        country.setName("Test country");
        country.setDescription("Test description");
        country.setPhoneCode("+380991112233");
        countries.add(country);
        given(this.countryRepository.findAll()).willReturn(countries);
        given(this.countryRepository.findAllById(country.getId())).willReturn(countries);
        given(this.countryRepository.findCountriesByName(country.getName())).willReturn(countries);
        given(this.countryRepository.findCountriesByCode(country.getCode())).willReturn(countries);
        this.countryService = new CountryServiceImpl(countryRepository);
    }

    @Test
    void getAllCountries() {
        List<Country> countries = countryService.getAllCountries();
        assertTrue(countries.get(0).getCode().equals(country.getCode()));
    }

    @Test
    void getCountriesById() {
        List<Country> countries = countryService.getCountriesById(country.getId());
        assertTrue(countries.get(0).getId().equals(country.getId()));
    }

    @Test
    void getCountriesByName() {
        List<Country> countries = countryService.getCountriesByName(country.getName());
        assertTrue(countries.get(0).getName().equals(country.getName()));
    }

    @Test
    void getCountriesByCode() {
        List<Country> countries = countryService.getCountriesByCode(country.getCode());
        assertTrue(countries.get(0).getCode().equals(country.getCode()));
    }
}

