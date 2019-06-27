/* ? unique key -> (country_code) */
INSERT INTO public.address_regional_scheme_used (id, country_code, list_schemes_id) VALUES (1, 'MD', 2 /* 'CUATM' */);
INSERT INTO public.address_regional_scheme_used (id, country_code, list_schemes_id) VALUES (2, 'BY', 3 /* 'BY-ISO' */);

/* unique key -> (list_schemes_id, country_code, code) */
INSERT INTO public.address_regional_schemes (id, list_schemes_id, country_code, code) VALUES (1, 2 /* 'CUATM' */,  'MD', 'REGION-1');
INSERT INTO public.address_regional_schemes (id, list_schemes_id, country_code, code) VALUES (2, 2 /* 'CUATM' */,  'MD', 'REGION-2');
INSERT INTO public.address_regional_schemes (id, list_schemes_id, country_code, code) VALUES (3, 3 /* 'BY-ISO' */, 'BY', 'REGION-1');
INSERT INTO public.address_regional_schemes (id, list_schemes_id, country_code, code) VALUES (4, 3 /* 'BY-ISO' */, 'BY', 'REGION-3');

INSERT INTO public.address_regional_schemes_i18n (id, address_regional_scheme_id, language_code, description) VALUES (1, 1 /* CUATM -> MD -> REGION-1 */, 'EN', 'Anenii Noi EN');
INSERT INTO public.address_regional_schemes_i18n (id, address_regional_scheme_id, language_code, description) VALUES (2, 1 /* CUATM -> MD -> REGION-1 */, 'RO', 'Anenii Noi RO');
INSERT INTO public.address_regional_schemes_i18n (id, address_regional_scheme_id, language_code, description) VALUES (3, 2 /* CUATM -> MD -> REGION-2 */, 'EN','Basarabeasca EN');
INSERT INTO public.address_regional_schemes_i18n (id, address_regional_scheme_id, language_code, description) VALUES (4, 2 /* CUATM -> MD -> REGION-2 */, 'RO','Basarabeasca RO');
