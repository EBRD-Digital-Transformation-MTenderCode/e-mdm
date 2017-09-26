package com.procurement.mdm.service;

import com.procurement.mdm.model.entity.CountryEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CountryServiceImpl implements CountryService {
    @Override
    public List<CountryEntity> getAll() {
        return null;
    }

    @Override
    public List<CountryEntity> getCountriesByName(String name) {
        return null;
    }

    @Override
    public List<CountryEntity> getCountriesByCode(String code) {
        return null;
    }
}
