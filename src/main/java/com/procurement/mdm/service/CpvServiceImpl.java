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
    public List<Cpv> getCpvByParam(Long language_id) {
        Objects.requireNonNull(language_id);
        return cpvRepository.findCpvsByLanguage_Id(language_id);
    }

    @Override
    public List<Cpv> getCpvByParam(Long language_id, Integer group) {
        Objects.requireNonNull(language_id);
        Objects.requireNonNull(group);
        return cpvRepository.findCpvsByLanguage_IdAndGroup(language_id, group);
    }

    @Override
    public List<Cpv> getCpvByParam(Long language_id, String parent) {
        Objects.requireNonNull(language_id);
        Objects.requireNonNull(parent);
        return cpvRepository.findCpvsByLanguage_IdAndParent(language_id, parent);
    }

    @Override
    public List<Cpv> getCpvByParam(Long language_id, Integer group, String parent) {
        Objects.requireNonNull(language_id);
        Objects.requireNonNull(group);
        Objects.requireNonNull(parent);
        return cpvRepository.findCpvsByLanguage_IdAndGroupAndParent(language_id, group, parent);
    }

    @Override
    public List<Cpv> getCpvByLanguage(Long language_id) {
        return cpvRepository.findCpvsByLanguage_Id(language_id);
    }
}
