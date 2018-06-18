package com.procurement.mdm.service;

import com.procurement.mdm.model.entity.Country;
import com.procurement.mdm.repository.CountryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

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
    public List<Country> getCountriesByCode(String code) {
        Objects.requireNonNull(code);
        return countryRepository.findCountriesByCode(code);
    }

    @Override
    public List<Country> getCountriesByName(String name) {
        Objects.requireNonNull(name);
        return countryRepository.findCountriesByName(name);
    }
}
