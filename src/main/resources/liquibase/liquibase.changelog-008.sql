--liquibase formatted sql

--changeset kateshaidurova:countries
CREATE TABLE public.country_schemes (
  id              BIGINT     NOT NULL PRIMARY KEY,
  list_schemes_id BIGINT     NOT NULL REFERENCES list_schemes (id),
  code            VARCHAR(2) NOT NULL,
  UNIQUE (list_schemes_id, code)
);

--changeset kateshaidurova:country_schemes_filling_data context:!test
INSERT INTO public.country_schemes(id, list_schemes_id, code)
VALUES (1, 1 /* ISO-ALPHA2 */, 'MD');


--changeset kateshaidurova:country_schemes_i18n
CREATE TABLE public.country_schemes_i18n (
  id                        BIGINT     NOT NULL PRIMARY KEY,
  country_scheme_id BIGINT         NOT NULL REFERENCES country_schemes (id),
  language_code             VARCHAR(2) NOT NULL,
  description               TEXT       NOT NULL,
  UNIQUE (country_scheme_id, language_code)
);

--changeset kateshaidurova:country_schemes_i18n_filling_data context:!test
INSERT INTO public.country_schemes_i18n(id, country_scheme_id, language_code, description)
VALUES (1, 1, 'EN', 'MOLDOVA (REPUBLIC OF)'),
       (2, 1, 'RO', 'MOLDOVA'),
       (3, 1, 'RU', 'МОЛДОВА');
