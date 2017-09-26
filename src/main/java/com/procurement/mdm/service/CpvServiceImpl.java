package com.procurement.mdm.service;

import com.procurement.mdm.model.entity.CpvEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CpvServiceImpl implements CpvService {
    @Override
    public List<CpvEntity> getCpvByParam(Long language_id, String group, String parent) {
        return null;
    }

    @Override
    public List<CpvEntity> getCpvByLanguage(Long language_id) {
        return null;
    }
}
