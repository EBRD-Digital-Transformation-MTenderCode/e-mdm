package com.procurement.mdm.repositories;

import com.procurement.mdm.model.entity.CountryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CountryRepository extends JpaRepository<CountryEntity, Long> {

    List<CountryEntity> findCountryEntitiesByName(String name);

    List<CountryEntity> findCountryEntitiesByCode(String code);
}
