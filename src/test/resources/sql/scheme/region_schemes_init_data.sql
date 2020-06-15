INSERT INTO public.region_schemes(id, list_schemes_id, country_code, code)
VALUES (1, 2 /* CUATM */, 'MD', 'REGION-1'),
       (2, 2 /* CUATM */, 'MD', 'REGION-2');

INSERT INTO public.region_schemes_i18n(id, region_scheme_id, language_code, description)
VALUES (1, 1, 'EN', 'mun.Chisinau'),
       (2, 1, 'RO', 'mun.Chişinău'),
       (3, 1, 'RU', 'мун.Кишинэу');