package com.procurement.mdm.service;

import com.procurement.mdm.model.entity.Country;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CountryService {

    List<Country> getAllCountries();

    List<Country> getCountriesByCode(String code);

    List<Country> getCountriesByName(String name);
}
