package com.procurement.mdm.repositories;

import com.procurement.mdm.model.entity.Cpv;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CpvRepository extends JpaRepository<Cpv, String> {

    List<Cpv> findCpvsByLanguage_Id(Long language_id);

    List<Cpv> findCpvsByLanguage_IdAndGroupAndParent(Long language_id, Integer group, String parent);

    List<Cpv> findCpvsByLanguage_IdAndGroup(Long language_id, Integer group);

    List<Cpv> findCpvsByLanguage_IdAndParent(Long language_id, String parent);
}
