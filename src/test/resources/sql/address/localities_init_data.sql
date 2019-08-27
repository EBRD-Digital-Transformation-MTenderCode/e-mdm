/* ? unique key -> (country_code, region_code) */
INSERT INTO public.address_locality_scheme_used (id, country_code, region_code, list_schemes_id) VALUES (1, 'MD', 'REGION-1', 2 /* 'CUATM' */);
INSERT INTO public.address_locality_scheme_used (id, country_code, region_code, list_schemes_id) VALUES (2, 'MD', 'REGION-2', 2 /* 'CUATM' */);
INSERT INTO public.address_locality_scheme_used (id, country_code, region_code, list_schemes_id) VALUES (3, 'BY', 'REGION-1', 3 /* 'BY-ISO' */);
INSERT INTO public.address_locality_scheme_used (id, country_code, region_code, list_schemes_id) VALUES (4, 'BY', 'REGION-3', 3 /* 'BY-ISO' */);

/* unique key -> (scheme_code, country_code, region_code, code) */
INSERT INTO public.address_locality_schemes (id, list_schemes_id, country_code, region_code, code) VALUES (1, 2 /* 'CUATM' */, 'MD', 'REGION-1', 'LOCALITY-1');
INSERT INTO public.address_locality_schemes (id, list_schemes_id, country_code, region_code, code) VALUES (2, 2 /* 'CUATM' */, 'MD', 'REGION-1', 'LOCALITY-3');
INSERT INTO public.address_locality_schemes (id, list_schemes_id, country_code, region_code, code) VALUES (3, 2 /* 'CUATM' */, 'MD', 'REGION-2', 'LOCALITY-2');
INSERT INTO public.address_locality_schemes (id, list_schemes_id, country_code, region_code, code) VALUES (4, 2 /* 'CUATM' */, 'MD', 'REGION-2', 'LOCALITY-1');

INSERT INTO public.address_locality_schemes_i18n (id, address_locality_schemes_id, language_code, description) VALUES (1, 1 /* CUATM -> MD -> REGION-1 -> LOCALITY-1 */, 'EN', 'mun.Chisinau EN');
INSERT INTO public.address_locality_schemes_i18n (id, address_locality_schemes_id, language_code, description) VALUES (2, 1 /* CUATM -> MD -> REGION-1 -> LOCALITY-1 */, 'RO', 'mun.Chişinău RO');
INSERT INTO public.address_locality_schemes_i18n (id, address_locality_schemes_id, language_code, description) VALUES (3, 2 /* CUATM -> MD -> REGION-1 -> LOCALITY-3 */, 'EN', 'or.Singera EN');
INSERT INTO public.address_locality_schemes_i18n (id, address_locality_schemes_id, language_code, description) VALUES (4, 2 /* CUATM -> MD -> REGION-1 -> LOCALITY-3 */, 'RO', 'or.Sîngera RO');
INSERT INTO public.address_locality_schemes_i18n (id, address_locality_schemes_id, language_code, description) VALUES (5, 3 /* CUATM -> MD -> REGION-2 -> LOCALITY-2 */, 'EN', 's.Dobrogea EN');
INSERT INTO public.address_locality_schemes_i18n (id, address_locality_schemes_id, language_code, description) VALUES (6, 3 /* CUATM -> MD -> REGION-2 -> LOCALITY-2 */, 'RO', 's.Dobrogea RO');
INSERT INTO public.address_locality_schemes_i18n (id, address_locality_schemes_id, language_code, description) VALUES (7, 4 /* CUATM -> MD -> REGION-2 -> LOCALITY-1 */, 'EN', 'mun.Chisinau EN');
INSERT INTO public.address_locality_schemes_i18n (id, address_locality_schemes_id, language_code, description) VALUES (8, 4 /* CUATM -> MD -> REGION-2 -> LOCALITY-1 */, 'RO', 'mun.Chişinău RO');
