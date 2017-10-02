package com.procurement.mdm.service;

import com.procurement.mdm.model.entity.Cpv;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CpvService {

    List<Cpv> getCpvByParam(Long language_id);

    List<Cpv> getCpvByParam(Long language_id, Integer group);

    List<Cpv> getCpvByParam(Long language_id, String parent);

    List<Cpv> getCpvByParam(Long language_id, Integer group, String parent);

    List<Cpv> getCpvByLanguage(Long language_id);
}
