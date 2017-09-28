package com.procurement.mdm.service;


import com.procurement.mdm.model.entity.Country;

import java.util.List;

public interface CountryService {

    List<Country> getAllCountries();

    List<Country> getCountriesById(Long id);

    List<Country> getCountriesByName(String name);

    List<Country> getCountriesByCode(String code);

}
