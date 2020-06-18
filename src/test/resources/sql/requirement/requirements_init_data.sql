INSERT INTO public.requirements (id, country_code, pmd, phase, requirement_group_code, code)
VALUES (1, 'MD', 'OT', 'SUBMISSION', 'REQ_GROUP_1', 'REQ_1'),
       (2, 'MD', 'OT', 'SUBMISSION', 'REQ_GROUP_1', 'REQ_1_1'),
       (3, 'MD', 'OT', 'SUBMISSION', 'REQ_GROUP_2', 'REQ_2');


INSERT INTO public.requirements_i18n (id, requirement_id, language_code, title, description)
VALUES (1, 1, 'EN', 'req-title-1', 'req-description-1'),
       (2, 2, 'EN', 'req-title-1-1', 'req-description-1-1'),
       (3, 3, 'EN', 'req-title-2', NULL);




