--liquibase formatted sql

--changeset kateshaidurova:criteria
CREATE TABLE public.criteria (
  id                         BIGINT     NOT NULL PRIMARY KEY,
  country_code               VARCHAR(2) NOT NULL,
  pmd                        TEXT       NOT NULL,
  phase                      TEXT       NOT NULL,
  code                       TEXT       NOT NULL,
  UNIQUE (country_code, pmd, phase, code)
);

--changeset kateshaidurova:criteria_i18n
CREATE TABLE public.criteria_i18n (
  id                         BIGINT     NOT NULL PRIMARY KEY,
  criterion_id                BIGINT     NOT NULL REFERENCES criteria (id),
  language_code              VARCHAR(2) NOT NULL,
  title                      TEXT       NOT NULL,
  description                TEXT,
  UNIQUE (criterion_id, language_code)
);