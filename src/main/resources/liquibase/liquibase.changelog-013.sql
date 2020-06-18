--liquibase formatted sql

--changeset kateshaidurova:requirements
CREATE TABLE public.requirements (
  id                         BIGINT     NOT NULL PRIMARY KEY,
  country_code               VARCHAR(2) NOT NULL,
  pmd                        TEXT       NOT NULL,
  phase                      TEXT       NOT NULL,
  requirement_group_code     TEXT       NOT NULL,
  code                       TEXT       NOT NULL,
  UNIQUE (country_code, pmd, phase, requirement_group_code, code)
);

--changeset kateshaidurova:requirements_i18n
CREATE TABLE public.requirements_i18n (
  id                         BIGINT     NOT NULL PRIMARY KEY,
  requirement_id             BIGINT     NOT NULL REFERENCES requirements (id),
  language_code              VARCHAR(2) NOT NULL,
  title                      TEXT       NOT NULL,
  description                TEXT,
  UNIQUE (requirement_id, language_code)
);