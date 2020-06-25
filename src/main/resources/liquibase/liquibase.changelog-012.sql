--liquibase formatted sql

--changeset kateshaidurova:requirement_groups
CREATE TABLE public.requirement_groups (
  id                         BIGINT     NOT NULL PRIMARY KEY,
  country_code               VARCHAR(2) NOT NULL,
  pmd                        TEXT       NOT NULL,
  phase                      TEXT       NOT NULL,
  criterion_code              TEXT       NOT NULL,
  code                       TEXT       NOT NULL,
  UNIQUE (country_code, pmd, phase, criterion_code, code)
);

--changeset kateshaidurova:requirement_groups_i18n
CREATE TABLE public.requirement_groups_i18n (
  id                         BIGINT     NOT NULL PRIMARY KEY,
  requirement_group_id       BIGINT     NOT NULL REFERENCES requirement_groups (id),
  language_code              VARCHAR(2) NOT NULL,
  description                TEXT,
  UNIQUE (requirement_group_id, language_code)
);