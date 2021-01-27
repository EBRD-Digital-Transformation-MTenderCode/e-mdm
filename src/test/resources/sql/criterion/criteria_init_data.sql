INSERT INTO public.criteria_classification_schemes (id, code)
VALUES (1, 'scheme-1'),
       (2, 'scheme-2');

INSERT INTO public.criteria_classifications(id, code, scheme_id)
VALUES (1, 'classification-1', 1),
       (2, 'classification-2', 2);


INSERT INTO public.criteria (id, country_code, pmd, phase, code, classification_id)
VALUES (1, 'MD', 'OT', 'SUBMISSION', 'MD_OT_1', 1),
       (2, 'MD', 'OT', 'SUBMISSION', 'MD_OT_2', 2);


INSERT INTO public.criteria_i18n (id, criterion_id, language_code, title, description)
VALUES (1, 1, 'EN', 'criterion-title-1', 'criterion-description-1'),
       (2, 2, 'EN', 'criterion-title-2', 'criterion-description-2');




