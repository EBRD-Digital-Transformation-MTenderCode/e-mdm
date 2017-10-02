package com.procurement.mdm.service;

import com.procurement.mdm.model.entity.Cpv;
import com.procurement.mdm.repositories.CpvRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CpvServiceImpl implements CpvService {

    private CpvRepository cpvRepository;

    public CpvServiceImpl(CpvRepository cpvRepository) {
        this.cpvRepository = cpvRepository;
    }

    @Override
    public List<Cpv> getCpvByParam(Long language_id, Integer group, String parent) {
        if (group != null && parent != null) {
            return cpvRepository.findCpvsByLanguage_IdAndGroupAndParent(language_id, group, parent);
        } else if (group == null && parent == null) {
            return cpvRepository.findCpvsByLanguage_Id(language_id);
        } else if (group == null) {
            return cpvRepository.findCpvsByLanguage_IdAndParent(language_id, parent);
        } else if (parent == null) {
            return cpvRepository.findCpvsByLanguage_IdAndGroup(language_id, group);
        }
        return null;
    }

    @Override
    public List<Cpv> getCpvByLanguage(Long language_id) {
        return cpvRepository.findCpvsByLanguage_Id(language_id);
    }
}
