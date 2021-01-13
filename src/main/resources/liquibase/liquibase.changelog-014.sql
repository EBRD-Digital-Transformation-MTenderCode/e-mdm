--liquibase formatted sql

--changeset kopach:standard_criteria
CREATE TABLE standard_criteria
(
    id            TEXT PRIMARY KEY,
    country_code  VARCHAR(2) NOT NULL,
    language_code VARCHAR(2) NOT NULL,
    data          jsonb NOT NULL
);