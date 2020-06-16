package com.procurement.mdm.application.service.address

import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import com.procurement.mdm.application.exception.RegionDescriptionNotFoundException
import com.procurement.mdm.application.exception.RegionNotFoundException
import com.procurement.mdm.application.exception.RegionNotLinkedToCountryException
import com.procurement.mdm.application.exception.RegionSchemeNotFoundException
import com.procurement.mdm.domain.entity.RegionEntity
import com.procurement.mdm.domain.exception.LanguageUnknownException
import com.procurement.mdm.domain.model.code.CountryCode
import com.procurement.mdm.domain.model.code.LanguageCode
import com.procurement.mdm.domain.model.code.RegionCode
import com.procurement.mdm.domain.model.identifier.RegionIdentifier
import com.procurement.mdm.domain.model.scheme.RegionScheme
import com.procurement.mdm.domain.repository.AdvancedLanguageRepository
import com.procurement.mdm.domain.repository.address.AddressRegionRepository
import com.procurement.mdm.domain.repository.scheme.RegionSchemeRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class AddressRegionServiceTest {

    companion object {
        private const val COUNTRY = "md"
        private val COUNTRY_CODE = CountryCode(COUNTRY)

        private const val UNKNOWN_COUNTRY = "uc"
        private val UNKNOWN_COUNTRY_CODE = CountryCode(UNKNOWN_COUNTRY)

        private const val REGION = "REGION-2"
        private val REGION_CODE = RegionCode(REGION)

        private const val UNKNOWN_REGION = "UNKNOWN"
        private val UNKNOWN_REGION_CODE = RegionCode(UNKNOWN_REGION)

        private const val LANGUAGE = "ro"
        private val LANGUAGE_CODE = LanguageCode(LANGUAGE)

        private const val UNKNOWN_LANGUAGE = "ul"
        private val UNKNOWN_LANGUAGE_CODE = LanguageCode(UNKNOWN_LANGUAGE)

        private const val SCHEME = "CUATM"
        private val REGION_SCHEME = RegionScheme(SCHEME)
        private const val DESCRIPTION = "Basarabeasca RO"
        private const val URI = "http://statistica.md"

        private val REGION_ENTITY_FIRST = RegionEntity(
            scheme = SCHEME,
            id = REGION_CODE.value,
            description = DESCRIPTION,
            uri = URI
        )

        private val REGION_IDENTIFIER_FIRST = RegionIdentifier(
            scheme = REGION_ENTITY_FIRST.scheme,
            id = REGION_ENTITY_FIRST.id,
            description = REGION_ENTITY_FIRST.description,
            uri = REGION_ENTITY_FIRST.uri
        )
    }

    private lateinit var addressRegionRepository: AddressRegionRepository
    private lateinit var regionSchemeRepository: RegionSchemeRepository
    private lateinit var advancedLanguageRepository: AdvancedLanguageRepository
    private lateinit var service: AddressRegionService

    @BeforeEach
    fun init() {
        addressRegionRepository = mock()
        advancedLanguageRepository = mock()
        regionSchemeRepository = mock()

        service = AddressRegionServiceImpl(
            addressRegionRepository = addressRegionRepository,
            regionSchemeRepository = regionSchemeRepository,
            advancedLanguageRepository = advancedLanguageRepository
        )
    }

    @Test
    fun `Getting the region by code is successful`() {
        whenever(advancedLanguageRepository.exists(eq(LANGUAGE_CODE)))
            .thenReturn(true)
        whenever(
            addressRegionRepository.findBy(
                region = eq(REGION_CODE),
                country = eq(COUNTRY_CODE),
                language = eq(LANGUAGE_CODE)
            )
        ).thenReturn(REGION_ENTITY_FIRST)

        val result = service.getBy(region = REGION, country = COUNTRY, language = LANGUAGE, scheme = null)

        assertEquals(REGION_IDENTIFIER_FIRST, result)
    }

    @Test
    fun `Getting the region by code is error (language request parameter is unknown)`() {
        whenever(advancedLanguageRepository.exists(eq(UNKNOWN_LANGUAGE_CODE)))
            .thenReturn(false)

        val exception = assertThrows<LanguageUnknownException> {
            service.getBy(region = REGION, country = COUNTRY, language = UNKNOWN_LANGUAGE, scheme = null)
        }

        assertEquals("The unknown code of a language '$UNKNOWN_LANGUAGE'.", exception.description)
    }

    @Test
    fun `Getting the region by code is error (unknown id)`() {
        whenever(advancedLanguageRepository.exists(eq(LANGUAGE_CODE)))
            .thenReturn(true)

        whenever(
            addressRegionRepository.findBy(
                region = eq(UNKNOWN_REGION_CODE),
                country = eq(COUNTRY_CODE),
                language = eq(LANGUAGE_CODE)
            )
        ).thenReturn(null)

        val exception = assertThrows<RegionNotFoundException> {
            service.getBy(region = UNKNOWN_REGION, country = COUNTRY, language = LANGUAGE, scheme = null)
        }

        assertEquals(
            "The region by code '$UNKNOWN_REGION', country '$COUNTRY', language '$LANGUAGE' not found.",
            exception.description
        )
    }

    @Test
    fun `Getting the region by code is error (unknown country)`() {
        whenever(advancedLanguageRepository.exists(eq(LANGUAGE_CODE)))
            .thenReturn(true)

        whenever(
            addressRegionRepository.findBy(
                region = eq(REGION_CODE),
                country = eq(UNKNOWN_COUNTRY_CODE),
                language = eq(LANGUAGE_CODE)
            )
        ).thenReturn(null)

        val exception = assertThrows<RegionNotFoundException> {
            service.getBy(region = REGION, country = UNKNOWN_COUNTRY, language = LANGUAGE, scheme = null)
        }

        assertEquals(
            "The region by code '$REGION', country '$UNKNOWN_COUNTRY', language '$LANGUAGE' not found.",
            exception.description
        )
    }

    @Test
    fun `Getting the region by code is error (unknown language)`() {
        whenever(advancedLanguageRepository.exists(eq(UNKNOWN_LANGUAGE_CODE)))
            .thenReturn(true)

        whenever(
            addressRegionRepository.findBy(
                region = eq(REGION_CODE),
                country = eq(COUNTRY_CODE),
                language = eq(UNKNOWN_LANGUAGE_CODE)
            )
        ).thenReturn(null)

        val exception = assertThrows<RegionNotFoundException> {
            service.getBy(region = REGION, country = COUNTRY, language = UNKNOWN_LANGUAGE, scheme = null)
        }

        assertEquals(
            "The region by code '$REGION', country '$COUNTRY', language '$UNKNOWN_LANGUAGE' not found.",
            exception.description
        )
    }

    @Test
    fun `Getting the region by code with scheme is successful`() {
        whenever(advancedLanguageRepository.exists(eq(LANGUAGE_CODE)))
            .thenReturn(true)
        whenever(regionSchemeRepository.existsBy(eq(REGION_SCHEME)))
            .thenReturn(true)
        whenever(regionSchemeRepository.existsBy(region = eq(REGION_CODE), scheme = eq(REGION_SCHEME)))
            .thenReturn(true)
        whenever(
            regionSchemeRepository.existsBy(
                region = eq(REGION_CODE),
                scheme = eq(REGION_SCHEME),
                country = eq(COUNTRY_CODE)
            )
        ).thenReturn(true)

        whenever(
            regionSchemeRepository.findBy(
                region = eq(REGION_CODE),
                scheme = eq(REGION_SCHEME),
                country = eq(COUNTRY_CODE),
                language = eq(LANGUAGE_CODE)
            )
        ).thenReturn(REGION_ENTITY_FIRST)

        val result = service.getBy(region = REGION, country = COUNTRY, language = LANGUAGE, scheme = SCHEME)

        assertEquals(REGION_IDENTIFIER_FIRST, result)
    }

    @Test
    fun `Getting the region by code with scheme is error (unknown language)`() {
        whenever(advancedLanguageRepository.exists(eq(UNKNOWN_LANGUAGE_CODE)))
            .thenReturn(false)

        val exception = assertThrows<LanguageUnknownException> {
            service.getBy(region = REGION, country = COUNTRY, language = UNKNOWN_LANGUAGE, scheme = SCHEME)
        }

        assertEquals("The unknown code of a language '$UNKNOWN_LANGUAGE'.", exception.description)
    }

    @Test
    fun `Getting the region by code with scheme is error (unknown scheme)`() {
        whenever(advancedLanguageRepository.exists(eq(LANGUAGE_CODE)))
            .thenReturn(true)
        whenever(regionSchemeRepository.existsBy(eq(REGION_SCHEME)))
            .thenReturn(false)

        val exception = assertThrows<RegionSchemeNotFoundException> {
            service.getBy(region = REGION, country = COUNTRY, language = LANGUAGE, scheme = SCHEME)
        }

        assertEquals("Region scheme '$SCHEME' not found.", exception.description)
    }

    @Test
    fun `Getting the region by code with scheme is error (unknown region code)`() {
        whenever(advancedLanguageRepository.exists(eq(LANGUAGE_CODE)))
            .thenReturn(true)

        whenever(regionSchemeRepository.existsBy(eq(REGION_SCHEME)))
            .thenReturn(true)

        whenever(regionSchemeRepository.existsBy(region = eq(REGION_CODE), scheme = eq(REGION_SCHEME)))
            .thenReturn(false)

        val exception = assertThrows<RegionNotFoundException> {
            service.getBy(region = REGION, country = COUNTRY, language = LANGUAGE, scheme = SCHEME)
        }

        assertEquals("The region by code '$REGION' and scheme '$SCHEME' not found.", exception.description)
    }

    @Test
    fun `Getting the region by code with scheme is error (wrong country code)`() {
        whenever(advancedLanguageRepository.exists(eq(LANGUAGE_CODE)))
            .thenReturn(true)

        whenever(regionSchemeRepository.existsBy(eq(REGION_SCHEME)))
            .thenReturn(true)

        whenever(regionSchemeRepository.existsBy(region = eq(REGION_CODE), scheme = eq(REGION_SCHEME)))
            .thenReturn(true)

        whenever(
            regionSchemeRepository.existsBy(
                region = eq(REGION_CODE),
                scheme = eq(REGION_SCHEME),
                country = eq(COUNTRY_CODE)
            )
        ).thenReturn(false)

        val exception = assertThrows<RegionNotLinkedToCountryException> {
            service.getBy(region = REGION, country = COUNTRY, language = LANGUAGE, scheme = SCHEME)
        }

        assertEquals(
            "The region by code '$REGION' and scheme '$SCHEME' is not linked to country '$COUNTRY'.",
            exception.description
        )
    }

    @Test
    fun `Getting the region by code with scheme is error (no description found)`() {
        whenever(advancedLanguageRepository.exists(eq(LANGUAGE_CODE)))
            .thenReturn(true)

        whenever(regionSchemeRepository.existsBy(eq(REGION_SCHEME)))
            .thenReturn(true)

        whenever(regionSchemeRepository.existsBy(region = eq(REGION_CODE), scheme = eq(REGION_SCHEME)))
            .thenReturn(true)

        whenever(
            regionSchemeRepository.existsBy(
                region = eq(REGION_CODE),
                scheme = eq(REGION_SCHEME),
                country = eq(COUNTRY_CODE)
            )
        ).thenReturn(true)

        whenever(
            regionSchemeRepository.findBy(
                region = eq(REGION_CODE),
                scheme = eq(REGION_SCHEME),
                country = eq(COUNTRY_CODE),
                language = eq(LANGUAGE_CODE)
            )
        ).thenReturn(null)

        val exception = assertThrows<RegionDescriptionNotFoundException> {
            service.getBy(region = REGION, country = COUNTRY, language = LANGUAGE, scheme = SCHEME)
        }

        assertEquals(
            "The region '$REGION' description in language '$LANGUAGE' not found.",
            exception.description
        )
    }
}
