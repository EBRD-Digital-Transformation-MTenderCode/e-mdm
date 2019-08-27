--liquibase formatted sql

--changeset msambulat:languages
CREATE TABLE public.languages (
  id          BIGINT     NOT NULL PRIMARY KEY,
  code        VARCHAR(2) NOT NULL UNIQUE,
  description TEXT       NOT NULL DEFAULT ''
);

--changeset msambulat:languages_init context:!test
INSERT INTO languages(id, code, description)
VALUES (1, 'EN', 'English'),
       (2, 'RO', 'Romanian; Moldavian; Moldovan'),
       (3, 'RU', 'Russian');


--changeset msambulat:list_schemes
CREATE TABLE public.list_schemes (
  id   BIGINT NOT NULL PRIMARY KEY,
  code TEXT   NOT NULL UNIQUE,
  uri  TEXT   NOT NULL DEFAULT ''
);

--changeset msambulat:schemes_descriptions_filling_data context:!test
INSERT INTO list_schemes(id, code, uri)
VALUES (1, 'ISO-ALPHA2', 'http://reference.iatistandard.org'),
       (2, 'CUATM', 'http://statistica.md'),
       (3, 'MD-IDNO', '');


--changeset msambulat:address_country_scheme_used
CREATE TABLE public.address_country_scheme_used (
  id              BIGINT NOT NULL PRIMARY KEY,
  list_schemes_id BIGINT NOT NULL REFERENCES list_schemes (id) UNIQUE
);

--changeset msambulat:address_country_scheme_used_filling_data context:!test
INSERT INTO public.address_country_scheme_used(id, list_schemes_id)
VALUES (1, 1 /* ISO-ALPHA2 */);


--changeset msambulat:address_country_schemes
CREATE TABLE public.address_country_schemes (
  id              BIGINT     NOT NULL PRIMARY KEY,
  list_schemes_id BIGINT     NOT NULL REFERENCES list_schemes (id),
  code            VARCHAR(2) NOT NULL,
  UNIQUE (list_schemes_id, code)
);

--changeset msambulat:address_country_schemes_filling_data context:!test
INSERT INTO public.address_country_schemes(id, list_schemes_id, code)
VALUES (1, 1 /* ISO-ALPHA2 */, 'MD');


--changeset msambulat:country_schemes_i18n
CREATE TABLE public.address_country_schemes_i18n (
  id                        BIGINT     NOT NULL PRIMARY KEY,
  address_country_scheme_id BIGINT     NOT NULL REFERENCES address_country_schemes (id),
  language_code             VARCHAR(2) NOT NULL,
  description               TEXT       NOT NULL,
  UNIQUE (address_country_scheme_id, language_code)
);

--changeset msambulat:country_schemes_i18n_filling_data context:!test
INSERT INTO public.address_country_schemes_i18n(id, address_country_scheme_id, language_code, description)
VALUES (1, 1, 'EN', 'MOLDOVA (REPUBLIC OF)'),
       (2, 1, 'RO', 'MOLDOVA'),
       (3, 1, 'RU', 'МОЛДОВА');
