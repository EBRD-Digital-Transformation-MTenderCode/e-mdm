INSERT INTO public.country_schemes(id, list_schemes_id, code)
VALUES (1, 1 /* ISO */, 'MD'),
       (2, 1 /* ISO */, 'FR');

INSERT INTO public.country_schemes_i18n(id, country_scheme_id, language_code, description)
VALUES (1, 1, 'EN', 'MOLDOVA (REPUBLIC OF)'),
       (2, 1, 'RO', 'MOLDOVA'),
       (3, 1, 'RU', 'МОЛДОВА');