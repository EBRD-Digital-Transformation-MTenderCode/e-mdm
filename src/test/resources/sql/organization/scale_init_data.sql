/* ? unique key -> (code) */
INSERT INTO public.organization_scales (id, code) VALUES (1, 'MICRO');
INSERT INTO public.organization_scales (id, code) VALUES (2, 'SME');
INSERT INTO public.organization_scales (id, code) VALUES (3, 'LARGE');
INSERT INTO public.organization_scales (id, code) VALUES (4, '');

/* unique key -> (country_code, scale_id) */
INSERT INTO public.organization_scale_used (id, country_code, scale_id) VALUES (1, 'MD', 1 /* 'MICRO' */);
INSERT INTO public.organization_scale_used (id, country_code, scale_id) VALUES (2, 'MD', 2 /* 'SME' */);
INSERT INTO public.organization_scale_used (id, country_code, scale_id) VALUES (3, 'BY', 3 /* 'LARGE' */);
INSERT INTO public.organization_scale_used (id, country_code, scale_id) VALUES (4, 'BY', 4 /* '' */);
