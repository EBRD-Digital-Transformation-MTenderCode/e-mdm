/* ? unique key -> (scheme_code) */
INSERT INTO public.address_country_scheme_used (id, list_schemes_id) VALUES (1, 1/* 'ISO' */);

/* unique key -> (scheme_code, code) */
INSERT INTO public.address_country_schemes (id, list_schemes_id, code) VALUES (1, 1 /* 'ISO' */, 'MD'); /*  1 /* MD */  */
INSERT INTO public.address_country_schemes (id, list_schemes_id, code) VALUES (2, 1 /* 'ISO' */, 'BY'); /*  2 /* BY */  */

INSERT INTO public.address_country_schemes_i18n (id, address_country_scheme_id, language_code, description) VALUES (1, 1 /* ISO -> MD */, 'EN', 'MOLDOVA (REPUBLIC OF)');
INSERT INTO public.address_country_schemes_i18n (id, address_country_scheme_id, language_code, description) VALUES (2, 1 /* ISO -> MD */, 'RO', 'MOLDOVA');
INSERT INTO public.address_country_schemes_i18n (id, address_country_scheme_id, language_code, description) VALUES (3, 2 /* ISO -> BY */, 'EN', 'BELARUS');
INSERT INTO public.address_country_schemes_i18n (id, address_country_scheme_id, language_code, description) VALUES (4, 2 /* ISO -> BY */, 'RO', 'BIELORUSIA');
