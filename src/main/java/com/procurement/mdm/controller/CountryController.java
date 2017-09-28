package com.procurement.mdm.controller;

import com.procurement.mdm.model.entity.Country;
import com.procurement.mdm.service.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/countries")
public class CountryController {

    @Autowired
    CountryService countryService;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<Country>> getCountries() {
        List<Country> countries = countryService.getAllCountries();
        return new ResponseEntity<>(countries, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<List<Country>> getCountriesById(@PathVariable("id") Long id) {
        List<Country> countries = countryService.getCountriesById(id);
        return new ResponseEntity<>(countries, HttpStatus.OK);
    }

    @RequestMapping(value = "/byName", method = RequestMethod.GET)
    public ResponseEntity<List<Country>> getCountriesByName(@RequestParam String name) {
        List<Country> countries = countryService.getCountriesByName(name);
        return new ResponseEntity<>(countries, HttpStatus.OK);
    }

    @RequestMapping(value = "/byCode", method = RequestMethod.GET)
    public ResponseEntity<List<Country>> getCountriesByCode(@RequestParam String code) {
        List<Country> countries = countryService.getCountriesByCode(code);
        return new ResponseEntity<>(countries, HttpStatus.OK);
    }

}
