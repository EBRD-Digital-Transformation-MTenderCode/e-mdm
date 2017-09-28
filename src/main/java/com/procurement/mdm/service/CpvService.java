package com.procurement.mdm.service;

import com.procurement.mdm.model.entity.Cpv;

import java.util.List;

public interface CpvService {

    List<Cpv> getCpvByParam(Long language_id, Integer group, String parent);

    List<Cpv> getCpvByLanguage(Long language_id);
}
