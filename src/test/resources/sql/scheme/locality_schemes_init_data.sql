INSERT INTO public.locality_schemes(id, list_schemes_id, region_code, code)
VALUES (1, 2 /* CUATM */, 'REGION-1', 'LOCALITY-1'),
       (2, 2 /* CUATM */, 'REGION-2', 'LOCALITY-2');

INSERT INTO public.locality_schemes_i18n(id, locality_scheme_id, language_code, description)
VALUES (1, 1, 'RU', 'мун.Кишинэу'),
       (2, 1, 'RO', 'mun.Chişinău'),
       (3, 1, 'EN', 'mun.Chisinau');