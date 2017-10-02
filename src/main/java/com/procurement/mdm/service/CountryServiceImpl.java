package com.procurement.mdm.service;

import com.procurement.mdm.model.entity.Country;
import com.procurement.mdm.repositories.CountryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CountryServiceImpl implements CountryService {

    private CountryRepository countryRepository;

    public CountryServiceImpl(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    @Override
    public List<Country> getAllCountries() {
        return countryRepository.findAll();
    }

    @Override
    public List<Country> getCountriesById(Long id) {
        return countryRepository.findAllById(id);
    }

    @Override
    public List<Country> getCountriesByName(String name) {
        return countryRepository.findCountriesByName(name);
    }

    @Override
    public List<Country> getCountriesByCode(String code) {
        return countryRepository.findCountriesByCode(code);
    }
}
