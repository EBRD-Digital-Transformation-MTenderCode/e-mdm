--liquibase formatted sql

--changeset kateshaidurova:countries
CREATE TABLE public.countries (
  id              BIGINT     NOT NULL PRIMARY KEY,
  list_schemes_id BIGINT     NOT NULL REFERENCES list_schemes (id),
  code            VARCHAR(2) NOT NULL,
  UNIQUE (list_schemes_id, code)
);

--changeset kateshaidurova:countries_filling_data context:!test
INSERT INTO public.countries(id, list_schemes_id, code)
VALUES (1, 1 /* ISO-ALPHA2 */, 'MD');


--changeset kateshaidurova:countries_i18n
CREATE TABLE public.countries_i18n (
  id                        BIGINT     NOT NULL PRIMARY KEY,
  country_id BIGINT         NOT NULL REFERENCES countries (id),
  language_code             VARCHAR(2) NOT NULL,
  description               TEXT       NOT NULL,
  UNIQUE (country_id, language_code)
);

--changeset kateshaidurova:countries_i18n_filling_data context:!test
INSERT INTO public.countries_i18n(id, country_id, language_code, description)
VALUES (1, 1, 'EN', 'MOLDOVA (REPUBLIC OF)'),
       (2, 1, 'RO', 'MOLDOVA'),
       (3, 1, 'RU', 'МОЛДОВА');
