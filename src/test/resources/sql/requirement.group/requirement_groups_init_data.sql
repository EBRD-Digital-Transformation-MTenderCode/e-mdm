
INSERT INTO public.requirement_groups (id, country_code, pmd, phase, criterion_code, code)
VALUES (1, 'MD', 'OT', 'SUBMISSION', 'MD_OT_1', 'REQ_1'),
       (2, 'MD', 'OT', 'SUBMISSION', 'MD_OT_1', 'REQ_1_1'),
       (3, 'MD', 'OT', 'SUBMISSION', 'MD_OT_2', 'REQ_2'),
       (4, 'MD', 'OT', 'SUBMISSION', 'MD_OT_3', 'REQ_3');


INSERT INTO public.requirement_groups_i18n (id, requirement_group_id, language_code, description)
VALUES (1, 1, 'EN', 'req-description-1'),
       (2, 2, 'EN', 'req-description-1-1'),
       (3, 3, 'EN', NULL);




