package com.procurement.mdm.repositories;

import com.procurement.mdm.model.entity.CountryEntity;
import com.procurement.mdm.model.entity.CpvEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CpvRepository extends JpaRepository<CpvEntity, String> {

    List<CpvEntity> findCpvEntitiesByLanguage_Id(Long language_id);

    List<CountryEntity> findCpvEntitiesByLanguage_IdAndGroupAndParent(Long language_id, String group, String parent);
}
