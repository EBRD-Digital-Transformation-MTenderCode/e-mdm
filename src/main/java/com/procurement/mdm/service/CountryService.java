package com.procurement.mdm.service;


import com.procurement.mdm.model.entity.CountryEntity;

import java.util.List;

public interface CountryService {

    List<CountryEntity> getAll();

    List<CountryEntity> getCountriesByName(String name);

    List<CountryEntity> getCountriesByCode(String code);

}
