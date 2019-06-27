--liquibase formatted sql

--changeset msambulat:create_old_structure
CREATE TABLE IF NOT EXISTS entity_kind (
  code TEXT NOT NULL
    CONSTRAINT entity_kind_pkey PRIMARY KEY
);

CREATE TABLE IF NOT EXISTS language (
  code        VARCHAR(2) NOT NULL
    CONSTRAINT language_pkey PRIMARY KEY,
  description TEXT,
  name        TEXT
);

CREATE TABLE IF NOT EXISTS country (
  code          VARCHAR(2) NOT NULL,
  def           BOOLEAN,
  description   TEXT,
  name          TEXT,
  scheme        TEXT,
  uri           TEXT,
  language_code VARCHAR(2) NOT NULL
    CONSTRAINT fk_country_language REFERENCES language,
  CONSTRAINT country_pkey PRIMARY KEY (code, language_code)
);

CREATE TABLE IF NOT EXISTS cpvs (
  code          TEXT       NOT NULL,
  description   TEXT,
  name          TEXT,
  language_code VARCHAR(2) NOT NULL
    CONSTRAINT fk_cpvs_language REFERENCES language,
  CONSTRAINT cpvs_pkey PRIMARY KEY (code, language_code)
);

CREATE TABLE IF NOT EXISTS currency (
  code          VARCHAR(3) NOT NULL,
  def           BOOLEAN,
  description   TEXT,
  name          TEXT,
  language_code VARCHAR(2) NOT NULL
    CONSTRAINT fk_currency_language REFERENCES language,
  CONSTRAINT currency_pkey PRIMARY KEY (code, language_code)
);

CREATE TABLE IF NOT EXISTS currency_country (
  currency_code          VARCHAR(3) NOT NULL,
  currency_language_code VARCHAR(2) NOT NULL,
  country_code           VARCHAR(2) NOT NULL,
  country_language_code  VARCHAR(2) NOT NULL,
  CONSTRAINT currency_country_pkey PRIMARY KEY (currency_code, currency_language_code, country_code,
                                                country_language_code),
  CONSTRAINT fk_country FOREIGN KEY (country_code, country_language_code) REFERENCES country,
  CONSTRAINT fk_currency FOREIGN KEY (currency_code, currency_language_code) REFERENCES currency
);

CREATE TABLE IF NOT EXISTS document_type (
  code          TEXT       NOT NULL,
  description   TEXT,
  name          TEXT,
  language_code VARCHAR(2) NOT NULL
    CONSTRAINT fk_document_type_language REFERENCES language,
  CONSTRAINT document_type_pkey PRIMARY KEY (code, language_code)
);

CREATE TABLE IF NOT EXISTS document_type_entity_kind (
  document_type_code          TEXT       NOT NULL,
  document_type_language_code VARCHAR(2) NOT NULL,
  entity_kind_code            TEXT       NOT NULL
    CONSTRAINT fk_entity_kind_code REFERENCES entity_kind,
  CONSTRAINT document_type_entity_kind_pkey PRIMARY KEY (document_type_code, document_type_language_code, entity_kind_code),
  CONSTRAINT fk_document_type_code FOREIGN KEY (document_type_code, document_type_language_code) REFERENCES document_type
);

CREATE TABLE IF NOT EXISTS pmd (
  code                  TEXT       NOT NULL,
  description           TEXT,
  name                  TEXT,
  country_code          VARCHAR(2) NOT NULL,
  country_language_code VARCHAR(2) NOT NULL,
  CONSTRAINT pmd_pkey PRIMARY KEY (code, country_code, country_language_code),
  CONSTRAINT fk_pmd_country FOREIGN KEY (country_code, country_language_code) REFERENCES country
);

CREATE TABLE IF NOT EXISTS region (
  code                  TEXT       NOT NULL,
  description           TEXT,
  name                  TEXT,
  scheme                TEXT,
  uri                   TEXT,
  country_code          VARCHAR(2) NOT NULL,
  country_language_code VARCHAR(2) NOT NULL,
  CONSTRAINT region_pkey PRIMARY KEY (code, country_code, country_language_code),
  CONSTRAINT fk_region_country FOREIGN KEY (country_code, country_language_code) REFERENCES country
);

CREATE TABLE IF NOT EXISTS locality (
  code                         TEXT       NOT NULL,
  description                  TEXT,
  name                         TEXT,
  scheme                       TEXT,
  uri                          TEXT,
  region_code                  TEXT       NOT NULL,
  region_country_code          VARCHAR(2) NOT NULL,
  region_country_language_code VARCHAR(2) NOT NULL,
  CONSTRAINT locality_pkey PRIMARY KEY (code, region_code, region_country_code, region_country_language_code),
  CONSTRAINT fk_locality_region FOREIGN KEY (region_code, region_country_code, region_country_language_code) REFERENCES region
);

CREATE TABLE IF NOT EXISTS registration_scheme (
  code                  TEXT       NOT NULL,
  description           TEXT,
  name                  TEXT,
  scheme                TEXT,
  uri                   TEXT,
  country_code          VARCHAR(2) NOT NULL,
  country_language_code VARCHAR(2) NOT NULL,
  CONSTRAINT registration_scheme_pkey PRIMARY KEY (code, country_code, country_language_code),
  CONSTRAINT fk_registration_scheme_country FOREIGN KEY (country_code, country_language_code) REFERENCES country
);

CREATE TABLE IF NOT EXISTS translate (
  code          TEXT       NOT NULL,
  description   TEXT,
  name          TEXT,
  language_code VARCHAR(2) NOT NULL
    CONSTRAINT fk_translate_language REFERENCES language,
  CONSTRAINT translate_pkey PRIMARY KEY (code, language_code)
);

CREATE TABLE IF NOT EXISTS unit (
  code            TEXT       NOT NULL,
  description     TEXT,
  name            TEXT,
  unit_class_code TEXT,
  language_code   VARCHAR(2) NOT NULL
    CONSTRAINT fk_unit_language REFERENCES language,
  CONSTRAINT unit_pkey PRIMARY KEY (code, language_code)
);

CREATE TABLE IF NOT EXISTS unit_class (
  code          TEXT       NOT NULL,
  description   TEXT,
  name          TEXT,
  language_code VARCHAR(2) NOT NULL
    CONSTRAINT fk_unit_class_language REFERENCES language,
  CONSTRAINT unit_class_pkey PRIMARY KEY (code, language_code)
);

CREATE TABLE IF NOT EXISTS cpv (
  code                 TEXT       NOT NULL,
  description          TEXT,
  procurement_category TEXT,
  name                 TEXT,
  parent               TEXT,
  language_code        VARCHAR(2) NOT NULL
    CONSTRAINT fk_cpv_language REFERENCES language,
  CONSTRAINT cpv_pkey PRIMARY KEY (code, language_code)
);
