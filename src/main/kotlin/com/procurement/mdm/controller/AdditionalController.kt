package com.procurement.mdm.controller

//import com.procurement.mdm.service.AdditionalService
//import com.procurement.mdm.service.ValidationService
//
//@RestController
//@CrossOrigin(maxAge = 3600)
//@RequestMapping("/additional")
//class AdditionalController(private val validationService: ValidationService,
//                           private val additionalService: AdditionalService) {
//
//    @GetMapping("/holidays")
//    fun getHolidays(@RequestParam lang: String,
//                    @RequestParam country: String,
//                    @RequestParam(required = false) internal: Boolean = false): ResponseEntity<ResponseDto> {
//
//        validationService.country(lang.toUpperCase(), country.toUpperCase(), internal)
//        return ResponseEntity(
//                additionalService.getHolidays(lang.toUpperCase(), country.toUpperCase(), internal),
//                HttpStatus.OK)
//    }
//
//    @GetMapping("/bank")
//    fun getBank(@RequestParam lang: String,
//                @RequestParam country: String,
//                @RequestParam(required = false) internal: Boolean = false): ResponseEntity<ResponseDto> {
//        validationService.country(lang.toUpperCase(), country.toUpperCase(), internal)
//        return ResponseEntity(
//                additionalService.getBank(lang.toUpperCase(), country.toUpperCase(), internal),
//                HttpStatus.OK)
//    }
//
//    @GetMapping("/gpaAnnexes")
//    fun getGPAnnexes(@RequestParam lang: String,
//                     @RequestParam country: String,
//                     @RequestParam(required = false) internal: Boolean = false): ResponseEntity<ResponseDto> {
//        validationService.country(lang.toUpperCase(), country.toUpperCase(), internal)
//        return ResponseEntity(
//                additionalService.getGPAnnexes(lang.toUpperCase(), country.toUpperCase(), internal),
//                HttpStatus.OK)
//    }
//}
