--liquibase formatted sql

--changeset msambulat:organization_scales
CREATE TABLE public.organization_scales (
  id   BIGINT NOT NULL PRIMARY KEY,
  code TEXT   NOT NULL UNIQUE
);

--changeset msambulat:organization_scales_filling_data context:!test
INSERT INTO public.organization_scales (id, code)
VALUES (1, 'MICRO');
INSERT INTO public.organization_scales (id, code)
VALUES (2, 'SME');
INSERT INTO public.organization_scales (id, code)
VALUES (3, 'LARGE');
INSERT INTO public.organization_scales (id, code)
VALUES (4, '');

--changeset msambulat:organization_scale_used
CREATE TABLE public.organization_scale_used (
  id           BIGINT NOT NULL PRIMARY KEY,
  country_code TEXT   NOT NULL,
  scale_id     BIGINT NOT NULL REFERENCES organization_scales (id),
  UNIQUE (country_code, scale_id)
);

--changeset msambulat:organization_scale_used_filling_data context:!test
INSERT INTO public.organization_scale_used (id, country_code, scale_id)
VALUES (1, 'MD', 1 /* 'MICRO' */);
INSERT INTO public.organization_scale_used (id, country_code, scale_id)
VALUES (2, 'MD', 2 /* 'SME' */);
INSERT INTO public.organization_scale_used (id, country_code, scale_id)
VALUES (3, 'MD', 3 /* 'LARGE' */);
INSERT INTO public.organization_scale_used (id, country_code, scale_id)
VALUES (4, 'MD', 4 /* '' */);

