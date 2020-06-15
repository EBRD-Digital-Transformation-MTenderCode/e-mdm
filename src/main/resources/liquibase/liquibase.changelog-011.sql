--liquibase formatted sql

--changeset kateshaidurova:criterion
CREATE TABLE public.criterion (
  id                         BIGINT     NOT NULL PRIMARY KEY,
  country_code               VARCHAR(2) NOT NULL,
  pmd                        TEXT       NOT NULL,
  phase                      TEXT       NOT NULL,
  code                       TEXT       NOT NULL,
  UNIQUE (country_code, pmd, phase, code)
);

--changeset kateshaidurova:criterion_i18n
CREATE TABLE public.criterion_i18n (
  id                         BIGINT     NOT NULL PRIMARY KEY,
  criteria_id                BIGINT     NOT NULL REFERENCES criterion (id),
  language_code              VARCHAR(2) NOT NULL,
  title                      TEXT       NOT NULL,
  description                TEXT       NOT NULL DEFAULT '',
  UNIQUE (criteria_id, language_code)
);