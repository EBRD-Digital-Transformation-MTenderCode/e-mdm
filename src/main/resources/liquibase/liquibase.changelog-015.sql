--liquibase formatted sql

--changeset kateshaidurova:criteria_classification_schemes
CREATE TABLE public.criteria_classification_schemes
(
    id   BIGINT     NOT NULL PRIMARY KEY,
    code TEXT NOT NULL
);

--changeset kateshaidurova:criteria_classifications
CREATE TABLE public.criteria_classifications
(
    id        BIGINT     NOT NULL PRIMARY KEY,
    code      TEXT NOT NULL,
    scheme_id BIGINT     NOT NULL REFERENCES criteria_classification_schemes (id)
);

--changeset kateshaidurova:criteria
DROP TABLE public.criteria CASCADE;

CREATE TABLE public.criteria
(
    id                BIGINT     NOT NULL PRIMARY KEY,
    country_code      VARCHAR(2) NOT NULL,
    pmd               TEXT       NOT NULL,
    phase             TEXT       NOT NULL,
    code              TEXT       NOT NULL,
    classification_id BIGINT     NOT NULL REFERENCES criteria_classifications (id),
    UNIQUE (country_code, pmd, phase, code)
);

--changeset kateshaidurova:criteria_i18n
ALTER TABLE public.criteria_i18n ADD CONSTRAINT criteria_i18n_criterion_id_fkey FOREIGN KEY (criterion_id) REFERENCES criteria (id);





