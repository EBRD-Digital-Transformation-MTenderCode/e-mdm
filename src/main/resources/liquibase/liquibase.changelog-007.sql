--liquibase formatted sql

--changeset yevheniiskoryi:pmd context:!test
INSERT INTO public.pmd (code, description, name, country_code, country_language_code)
VALUES ('GPA', null, 'governmentProcurementAgreement', 'MD', 'en');
INSERT INTO public.pmd (code, description, name, country_code, country_language_code)
VALUES ('GPA', null, 'governmentProcurementAgreement', 'MD', 'ro');
