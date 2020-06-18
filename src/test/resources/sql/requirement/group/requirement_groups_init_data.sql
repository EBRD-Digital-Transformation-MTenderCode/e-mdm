
INSERT INTO public.requirement_groups (id, country_code, pmd, phase, criterion_code, code)
VALUES (1, 'MD', 'OT', 'SUBMISSION', 'MD_OT_1', 'REQ_GROUP_1'),
       (2, 'MD', 'OT', 'SUBMISSION', 'MD_OT_1', 'REQ_GROUP_1_1'),
       (3, 'MD', 'OT', 'SUBMISSION', 'MD_OT_2', 'REQ_GROUP_2'),
       (4, 'MD', 'OT', 'SUBMISSION', 'MD_OT_3', 'REQ_GROUP_3');


INSERT INTO public.requirement_groups_i18n (id, requirement_group_id, language_code, description)
VALUES (1, 1, 'EN', 'req-group-description-1'),
       (2, 2, 'EN', 'req-group-description-1-1'),
       (3, 3, 'EN', NULL);




