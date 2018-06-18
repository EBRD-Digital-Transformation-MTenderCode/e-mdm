package com.procurement.mdm.service

import com.procurement.mdm.model.entity.Cpv
import com.procurement.mdm.repository.CpvRepository
import org.springframework.stereotype.Service
import java.util.Objects

interface CpvService {

    fun getCpvByParam(languageCode: String): List<Cpv>

//    fun getCpvByParam(languageCode: String, group: Int?): List<Cpv>
//
//    fun getCpvByParam(languageCode: String, parent: String): List<Cpv>
//
//    fun getCpvByParam(languageCode: String, group: Int?, parent: String): List<Cpv>
//
//    fun getCpvByLanguageCode(languageCode: String): List<Cpv>
}


@Service
class CpvServiceImpl(private val cpvRepository: CpvRepository) : CpvService {

    override fun getCpvByParam(languageCode: String): List<Cpv> {
        Objects.requireNonNull(languageCode)
        return cpvRepository.findCpvsByLanguage(languageCode)
    }
//
//    override fun getCpvByParam(languageCode: String, group: Int?): List<Cpv> {
//        Objects.requireNonNull(languageCode)
//        Objects.requireNonNull<Int>(group)
//        return cpvRepository.findCpvsByLanguage_Iso6391AndGroup(languageCode, group)
//    }
//
//    override fun getCpvByParam(languageCode: String, parent: String): List<Cpv> {
//        Objects.requireNonNull(languageCode)
//        Objects.requireNonNull(parent)
//        return cpvRepository.findCpvsByLanguage_Iso6391AndParent(languageCode, parent)
//    }
//
//    override fun getCpvByParam(languageCode: String, group: Int?, parent: String): List<Cpv> {
//        Objects.requireNonNull(languageCode)
//        if (Objects.isNull(group) && Objects.isNull(parent)) {
//            return getCpvByParam(languageCode)
//        }
//        if (Objects.isNull(group) && Objects.nonNull(parent)) {
//            return getCpvByParam(languageCode, parent)
//        }
//        return if (Objects.nonNull(group) && Objects.isNull(parent)) {
//            getCpvByParam(languageCode, group)
//        } else cpvRepository.findCpvsByLanguage_Iso6391AndGroupAndParent(languageCode, group, parent)
//    }
//
//    override fun getCpvByLanguageCode(languageCode: String): List<Cpv> {
//        return cpvRepository.findCpvsByLanguage_Iso6391(languageCode)
//    }
}
