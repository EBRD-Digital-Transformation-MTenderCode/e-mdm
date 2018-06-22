//package com.procurement.mdm.repository
//
//import com.procurement.mdm.model.entity.Holidays
//import org.springframework.data.jpa.repository.JpaRepository
//import org.springframework.stereotype.Repository
//import org.springframework.transaction.annotation.Transactional
//
//@Repository
//interface HolidaysRepository : JpaRepository<Holidays, String> {
//
//    @Transactional(readOnly = true)
//    fun findByCountryId(countryId: String): List<Holidays>
//}
//
