package com.procurement.mdm.service;

import com.procurement.mdm.model.entity.CpvEntity;

import java.util.List;

public interface CpvService {

    List<CpvEntity> getCpvByParam(Long language_id, String group, String parent);

    List<CpvEntity> getCpvByLanguage(Long language_id);
}
