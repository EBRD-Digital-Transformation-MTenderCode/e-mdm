package com.procurement.mdm.service;

import com.procurement.mdm.model.entity.Cpv;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CpvService {

    List<Cpv> getCpvByParam(String languageCode);

    List<Cpv> getCpvByParam(String languageCode, Integer group);

    List<Cpv> getCpvByParam(String languageCode, String parent);

    List<Cpv> getCpvByParam(String languageCode, Integer group, String parent);

    List<Cpv> getCpvByLanguageCode(String languageCode);
}
