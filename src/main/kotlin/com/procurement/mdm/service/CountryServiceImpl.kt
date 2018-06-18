package com.procurement.mdm.service

import com.procurement.mdm.model.entity.Country
import com.procurement.mdm.repository.CountryRepository
import org.springframework.stereotype.Service
import java.util.Objects

interface CountryService {

    fun getAllCountries(): List<Country>

    fun getCountriesByCode(code: String): List<Country>

    fun getCountriesByName(name: String): List<Country>
}

@Service
class CountryServiceImpl(private val countryRepository: CountryRepository) : CountryService {

    override fun getAllCountries(): List<Country> {
        return countryRepository.findAll()
    }

    override fun getCountriesByCode(code: String): List<Country> {
        Objects.requireNonNull(code)
        return countryRepository.findCountriesByCode(code)
    }

    override fun getCountriesByName(name: String): List<Country> {
        Objects.requireNonNull(name)
        return countryRepository.findCountriesByName(name)
    }
}
