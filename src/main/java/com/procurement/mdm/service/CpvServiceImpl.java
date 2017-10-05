package com.procurement.mdm.service;

import com.procurement.mdm.model.entity.Cpv;
import com.procurement.mdm.repository.CpvRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class CpvServiceImpl implements CpvService {

    private CpvRepository cpvRepository;

    public CpvServiceImpl(CpvRepository cpvRepository) {
        this.cpvRepository = cpvRepository;
    }

    @Override
    public List<Cpv> getCpvByParam(String languageCode) {
        Objects.requireNonNull(languageCode);
        return cpvRepository.findCpvsByLanguage_Iso6391(languageCode);
    }

    @Override
    public List<Cpv> getCpvByParam(String languageCode, Integer group) {
        Objects.requireNonNull(languageCode);
        Objects.requireNonNull(group);
        return cpvRepository.findCpvsByLanguage_Iso6391AndGroup(languageCode, group);
    }

    @Override
    public List<Cpv> getCpvByParam(String languageCode, String parent) {
        Objects.requireNonNull(languageCode);
        Objects.requireNonNull(parent);
        return cpvRepository.findCpvsByLanguage_Iso6391AndParent(languageCode, parent);
    }

    @Override
    public List<Cpv> getCpvByParam(String languageCode, Integer group, String parent) {
        Objects.requireNonNull(languageCode);
        Objects.requireNonNull(group);
        Objects.requireNonNull(parent);
        return cpvRepository.findCpvsByLanguage_Iso6391AndGroupAndParent(languageCode, group, parent);
    }

    @Override
    public List<Cpv> getCpvByLanguageCode(String languageCode) {
        return cpvRepository.findCpvsByLanguage_Iso6391(languageCode);
    }
}
