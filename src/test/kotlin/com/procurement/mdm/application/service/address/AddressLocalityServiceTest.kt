package com.procurement.mdm.application.service.address

import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import com.procurement.mdm.application.exception.IdNotFoundException
import com.procurement.mdm.application.exception.LocalityNotFoundException
import com.procurement.mdm.application.exception.LocalityNotLinkedToRegionException
import com.procurement.mdm.application.exception.RegionNotLinkedToCountryException
import com.procurement.mdm.application.exception.SchemeNotFoundException
import com.procurement.mdm.domain.entity.LocalityEntity
import com.procurement.mdm.domain.exception.LanguageUnknownException
import com.procurement.mdm.domain.model.code.CountryCode
import com.procurement.mdm.domain.model.code.LanguageCode
import com.procurement.mdm.domain.model.code.LocalityCode
import com.procurement.mdm.domain.model.code.RegionCode
import com.procurement.mdm.domain.model.identifier.LocalityIdentifier
import com.procurement.mdm.domain.model.scheme.LocalityScheme
import com.procurement.mdm.domain.repository.AdvancedLanguageRepository
import com.procurement.mdm.domain.repository.address.AddressLocalityRepository
import com.procurement.mdm.domain.repository.scheme.LocalitySchemeRepository
import com.procurement.mdm.domain.repository.scheme.RegionSchemeRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class AddressLocalityServiceTest {

    companion object {
        private const val COUNTRY = "md"
        private val COUNTRY_CODE = CountryCode(COUNTRY)

        private const val UNKNOWN_COUNTRY = "uc"
        private val UNKNOWN_COUNTRY_CODE = CountryCode(UNKNOWN_COUNTRY)

        private const val REGION = "REGION-2"
        private val REGION_CODE = RegionCode(REGION)

        private const val UNKNOWN_REGION = "UNKNOWN"
        private val UNKNOWN_REGION_CODE = RegionCode(UNKNOWN_REGION)

        private const val LOCALITY = "LOCALITY-1"
        private val LOCALITY_CODE = LocalityCode(LOCALITY)

        private const val UNKNOWN_LOCALITY = "UNKNOWN"
        private val UNKNOWN_LOCALITY_CODE = LocalityCode(UNKNOWN_LOCALITY)

        private const val LANGUAGE = "ro"
        private val LANGUAGE_CODE = LanguageCode(LANGUAGE)

        private const val UNKNOWN_LANGUAGE = "ul"
        private val UNKNOWN_LANGUAGE_CODE = LanguageCode(UNKNOWN_LANGUAGE)

        private const val SCHEME = "CUATM"
        private val LOCALITY_SCHEME = LocalityScheme(SCHEME)
        private const val DESCRIPTION = "Basarabeasca RO"
        private const val URI = "http://statistica.md"

        private val LOCALITY_ENTITY = LocalityEntity(
            scheme = SCHEME,
            id = LOCALITY_CODE.value,
            description = DESCRIPTION,
            uri = URI
        )

        private val LOCALITY_IDENTIFIER = LocalityIdentifier(
            scheme = LOCALITY_ENTITY.scheme,
            id = LOCALITY_ENTITY.id,
            description = LOCALITY_ENTITY.description,
            uri = LOCALITY_ENTITY.uri
        )
    }

    private lateinit var addressLocalityRepository: AddressLocalityRepository
    private lateinit var advancedLanguageRepository: AdvancedLanguageRepository
    private lateinit var localitySchemeRepository: LocalitySchemeRepository
    private lateinit var regionSchemeRepository: RegionSchemeRepository

    private lateinit var service: AddressLocalityService

    @BeforeEach
    fun init() {
        addressLocalityRepository = mock()
        advancedLanguageRepository = mock()
        localitySchemeRepository = mock()
        regionSchemeRepository = mock()

        service = AddressLocalityServiceImpl(
            addressLocalityRepository = addressLocalityRepository,
            advancedLanguageRepository = advancedLanguageRepository,
            localitySchemeRepository = localitySchemeRepository,
            regionSchemeRepository = regionSchemeRepository
        )
    }

    @Test
    fun `Getting the locality by code is successful`() {
        whenever(advancedLanguageRepository.exists(eq(LANGUAGE_CODE)))
            .thenReturn(true)
        whenever(
            addressLocalityRepository.findBy(
                locality = eq(LOCALITY_CODE),
                country = eq(COUNTRY_CODE),
                region = eq(REGION_CODE),
                language = eq(LANGUAGE_CODE)
            )
        ).thenReturn(LOCALITY_ENTITY)

        val result = service.getBy(
            locality = LOCALITY, country = COUNTRY, region = REGION, language = LANGUAGE, scheme = null
        )

        assertEquals(LOCALITY_IDENTIFIER, result)
    }

    @Test
    fun `Getting the locality by code is error (language request parameter is unknown)`() {
        whenever(advancedLanguageRepository.exists(eq(UNKNOWN_LANGUAGE_CODE)))
            .thenReturn(false)

        val exception = assertThrows<LanguageUnknownException> {
            service.getBy(
                locality = LOCALITY, country = COUNTRY, region = REGION, language = UNKNOWN_LANGUAGE, scheme = null
            )
        }

        assertEquals("The unknown code of a language '$UNKNOWN_LANGUAGE'.", exception.description)
    }

    @Test
    fun `Getting the locality by code is error(unknown id)`() {
        whenever(advancedLanguageRepository.exists(eq(LANGUAGE_CODE)))
            .thenReturn(true)
        whenever(
            addressLocalityRepository.findBy(
                locality = eq(UNKNOWN_LOCALITY_CODE),
                country = eq(COUNTRY_CODE),
                region = eq(REGION_CODE),
                language = eq(LANGUAGE_CODE)
            )
        ).thenReturn(null)

        val exception = assertThrows<LocalityNotFoundException> {
            service.getBy(
                locality = UNKNOWN_LOCALITY, country = COUNTRY, region = REGION, language = LANGUAGE, scheme = null
            )
        }

        assertEquals(
            "The locality by code '$UNKNOWN_LOCALITY', country '$COUNTRY', region '$REGION', language '$LANGUAGE' not found.",
            exception.description
        )
    }

    @Test
    fun `Getting the locality by code is error(unknown country)`() {
        whenever(advancedLanguageRepository.exists(eq(LANGUAGE_CODE)))
            .thenReturn(true)
        whenever(
            addressLocalityRepository.findBy(
                locality = eq(LOCALITY_CODE),
                country = eq(UNKNOWN_COUNTRY_CODE),
                region = eq(REGION_CODE),
                language = eq(LANGUAGE_CODE)
            )
        ).thenReturn(null)

        val exception = assertThrows<LocalityNotFoundException> {
            service.getBy(
                locality = LOCALITY, country = UNKNOWN_COUNTRY, region = REGION, language = LANGUAGE, scheme = null
            )
        }

        assertEquals(
            "The locality by code '$LOCALITY', country '$UNKNOWN_COUNTRY', region '$REGION', language '$LANGUAGE' not found.",
            exception.description
        )
    }

    @Test
    fun `Getting the locality by code is error(unknown region)`() {
        whenever(advancedLanguageRepository.exists(eq(LANGUAGE_CODE)))
            .thenReturn(true)
        whenever(
            addressLocalityRepository.findBy(
                locality = eq(LOCALITY_CODE),
                country = eq(COUNTRY_CODE),
                region = eq(UNKNOWN_REGION_CODE),
                language = eq(LANGUAGE_CODE)
            )
        ).thenReturn(null)

        val exception = assertThrows<LocalityNotFoundException> {
            service.getBy(
                locality = LOCALITY, country = COUNTRY, region = UNKNOWN_REGION, language = LANGUAGE, scheme = null
            )
        }

        assertEquals(
            "The locality by code '$LOCALITY', country '$COUNTRY', region '$UNKNOWN_REGION', language '$LANGUAGE' not found.",
            exception.description
        )
    }

    @Test
    fun `Getting all schemes of localities is successful`() {
        whenever(advancedLanguageRepository.exists(eq(LANGUAGE_CODE)))
            .thenReturn(true)
        whenever(addressLocalityRepository.findAllSchemes(country = eq(COUNTRY_CODE), region = eq(REGION_CODE)))
            .thenReturn(listOf(SCHEME))

        val result = service.getAllSchemes(country = COUNTRY, region = REGION)

        assertEquals(1, result.size)
        assertEquals(SCHEME, result[0])
    }

    @Test
    fun `Getting the locality by code with scheme is successful`() {
        whenever(localitySchemeRepository.existsBy(eq(LOCALITY_SCHEME)))
            .thenReturn(true)
        whenever(localitySchemeRepository.existsBy(eq(LOCALITY_SCHEME), eq(LOCALITY_CODE)))
            .thenReturn(true)
        whenever(localitySchemeRepository.existsBy(eq(LOCALITY_SCHEME), eq(LOCALITY_CODE), eq(REGION_CODE)))
            .thenReturn(true)
        whenever(regionSchemeRepository.existsBy(eq(REGION_CODE), eq(COUNTRY_CODE)))
            .thenReturn(true)
        whenever(
            localitySchemeRepository.findBy(
                scheme = eq(LOCALITY_SCHEME),
                locality = eq(LOCALITY_CODE),
                region = eq(REGION_CODE),
                language = eq(LANGUAGE_CODE)
            )
        )
            .thenReturn(LOCALITY_ENTITY)

        val result = service.getBy(
            locality = LOCALITY, country = COUNTRY, region = REGION, language = LANGUAGE, scheme = SCHEME
        )

        assertEquals(LOCALITY_IDENTIFIER, result)
    }

    @Test
    fun `Getting the locality by code with scheme fails (unknown scheme)`() {
        whenever(localitySchemeRepository.existsBy(eq(LOCALITY_SCHEME)))
            .thenReturn(false)

        val exception = assertThrows<SchemeNotFoundException> {
            service.getBy(
                locality = LOCALITY, country = COUNTRY, region = REGION, language = LANGUAGE, scheme = SCHEME
            )
        }

        assertEquals(
            "Locality scheme '$SCHEME' not found.",
            exception.description
        )
    }

    @Test
    fun `Getting the locality by code with scheme fails (unknown locality by scheme)`() {
        whenever(localitySchemeRepository.existsBy(eq(LOCALITY_SCHEME)))
            .thenReturn(true)
        whenever(localitySchemeRepository.existsBy(eq(LOCALITY_SCHEME), eq(LOCALITY_CODE)))
            .thenReturn(false)

        val exception = assertThrows<IdNotFoundException> {
            service.getBy(
                locality = LOCALITY, country = COUNTRY, region = REGION, language = LANGUAGE, scheme = SCHEME
            )
        }

        assertEquals(
            "Locality id '$LOCALITY' by scheme '$SCHEME' not found.",
            exception.description
        )
    }

    @Test
    fun `Getting the locality by code with scheme fails (unknown region)`() {
        whenever(localitySchemeRepository.existsBy(eq(LOCALITY_SCHEME)))
            .thenReturn(true)
        whenever(localitySchemeRepository.existsBy(eq(LOCALITY_SCHEME), eq(LOCALITY_CODE)))
            .thenReturn(true)
        whenever(localitySchemeRepository.existsBy(eq(LOCALITY_SCHEME), eq(LOCALITY_CODE), eq(REGION_CODE)))
            .thenReturn(false)

        val exception = assertThrows<LocalityNotLinkedToRegionException> {
            service.getBy(
                locality = LOCALITY, country = COUNTRY, region = REGION, language = LANGUAGE, scheme = SCHEME
            )
        }

        assertEquals(
            "The locality by code '$LOCALITY' and scheme '$SCHEME' is not linked to region '$REGION'.",
            exception.description
        )
    }

    @Test
    fun `Getting the locality by code with scheme fails (unknown country)`() {
        whenever(localitySchemeRepository.existsBy(eq(LOCALITY_SCHEME)))
            .thenReturn(true)
        whenever(localitySchemeRepository.existsBy(eq(LOCALITY_SCHEME), eq(LOCALITY_CODE)))
            .thenReturn(true)
        whenever(localitySchemeRepository.existsBy(eq(LOCALITY_SCHEME), eq(LOCALITY_CODE), eq(REGION_CODE)))
            .thenReturn(true)
        whenever(regionSchemeRepository.existsBy(eq(REGION_CODE), eq(COUNTRY_CODE)))
            .thenReturn(false)

        val exception = assertThrows<RegionNotLinkedToCountryException> {
            service.getBy(
                locality = LOCALITY, country = COUNTRY, region = REGION, language = LANGUAGE, scheme = SCHEME
            )
        }

        assertEquals(
            "The region by code '$REGION' is not linked to country '$COUNTRY'.",
            exception.description
        )
    }

    @Test
    fun `Getting the locality by code with scheme fails (unknown language)`() {
        whenever(localitySchemeRepository.existsBy(eq(LOCALITY_SCHEME)))
            .thenReturn(true)
        whenever(localitySchemeRepository.existsBy(eq(LOCALITY_SCHEME), eq(LOCALITY_CODE)))
            .thenReturn(true)
        whenever(localitySchemeRepository.existsBy(eq(LOCALITY_SCHEME), eq(LOCALITY_CODE), eq(REGION_CODE)))
            .thenReturn(true)
        whenever(regionSchemeRepository.existsBy(eq(REGION_CODE), eq(COUNTRY_CODE)))
            .thenReturn(true)
        whenever(
            localitySchemeRepository.findBy(
                scheme = eq(LOCALITY_SCHEME),
                locality = eq(LOCALITY_CODE),
                region = eq(REGION_CODE),
                language = eq(LANGUAGE_CODE)
            )
        ).thenReturn(null)

        val exception = assertThrows<LocalityNotFoundException> {
            service.getBy(
                locality = LOCALITY, country = COUNTRY, region = REGION, language = LANGUAGE, scheme = SCHEME
            )
        }

        assertEquals(
            "The locality by code '$LOCALITY', scheme '$SCHEME', country '$COUNTRY', region '$REGION', language '$LANGUAGE' not found.",
            exception.description
        )
    }
}
