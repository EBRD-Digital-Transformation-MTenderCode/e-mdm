--liquibase formatted sql

--changeset msambulat:organization_scheme_used
CREATE TABLE public.organization_scheme_used (
  id              BIGINT     NOT NULL PRIMARY KEY,
  country_code    VARCHAR(2) NOT NULL,
  list_schemes_id BIGINT     NOT NULL REFERENCES list_schemes (id),
  UNIQUE (country_code, list_schemes_id)
);

--changeset msambulat:organization_scheme_used_filling_data context:!test
INSERT INTO public.organization_scheme_used(id, country_code, list_schemes_id)
VALUES (1, 'MD', 3 /* MD-IDNO */);
