package com.procurement.mdm.repository;

import com.procurement.mdm.model.entity.Cpv;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CpvRepository extends JpaRepository<Cpv, String> {

    List<Cpv> findCpvsByLanguage_Iso6391(String languageCode);

    List<Cpv> findCpvsByLanguage_Iso6391AndGroupAndParent(String languageCode, Integer group, String parent);

    List<Cpv> findCpvsByLanguage_Iso6391AndGroup(String languageCode, Integer group);

    List<Cpv> findCpvsByLanguage_Iso6391AndParent(String languageCode, String parent);
}
