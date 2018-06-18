package com.procurement.mdm.controller;

import com.procurement.mdm.model.entity.Cpv;
import com.procurement.mdm.service.CpvService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/cpv")
public class CpvController {

    private CpvService cpvService;

    public CpvController(CpvService cpvService) {
        this.cpvService = cpvService;
    }

    @RequestMapping(value = "/byLanguage", method = RequestMethod.GET)
    public ResponseEntity<List<Cpv>> getCpvByLanguageCode(@RequestParam("languageCode") String languageCode) {
        List<Cpv> cpv = cpvService.getCpvByLanguageCode(languageCode);
        return new ResponseEntity<>(cpv, HttpStatus.OK);
    }

    @RequestMapping(value = "/byParam", method = RequestMethod.GET)
    public ResponseEntity<List<Cpv>> getCpvByParam(@RequestParam("languageCode") String languageCode,
                                                   @RequestParam(value = "group", required = false) Integer group,
                                                   @RequestParam(value = "parent", required = false) String parent) {

        List<Cpv> cpv = cpvService.getCpvByParam(languageCode, group, parent);
        return new ResponseEntity<>(cpv, HttpStatus.OK);
    }
}
