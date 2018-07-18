INSERT INTO language (code, description, name) VALUES ('en', '', 'English');
INSERT INTO language (code, description, name) VALUES ('ro', '', 'Romanian; Moldavian; Moldovan');
INSERT INTO language (code, description, name) VALUES ('ru', '', 'Russian');

INSERT INTO public.currency (code, def, description, name, language_code) VALUES ('EUR', false, '', '', 'en');
INSERT INTO public.currency (code, def, description, name, language_code) VALUES ('MDL', false, '', '', 'en');
INSERT INTO public.currency (code, def, description, name, language_code) VALUES ('USD', false, '', '', 'en');

INSERT INTO public.unit_class(code, description, name, language_code) VALUES ('L', 'length', 'length', 'en');
INSERT INTO public.unit_class(code, description, name, language_code) VALUES ('M', 'mass', 'mass', 'en');
INSERT INTO public.unit_class(code, description, name, language_code) VALUES ('TD', 'time', 'time', 'en');
INSERT INTO public.unit_class(code, description, name, language_code) VALUES ('I', 'electric current', 'electric current', 'en');
INSERT INTO public.unit_class(code, description, name, language_code) VALUES ('Θ', 'thermodynamic temperature', 'thermodynamic temperature', 'en');
INSERT INTO public.unit_class(code, description, name, language_code) VALUES ('N', 'amount of substance', 'amount of substance', 'en');
INSERT INTO public.unit_class(code, description, name, language_code) VALUES ('J', 'luminous intensity', 'luminous intensity', 'en');

INSERT INTO public.unit (code, description, name, unit_class_language_code, unit_class_code) VALUES ('m', 'metre', 'metre', 'en', 'L');
INSERT INTO public.unit (code, description, name, unit_class_language_code, unit_class_code) VALUES ('kg', 'kilogram', 'kilogram', 'en', 'M');
INSERT INTO public.unit (code, description, name, unit_class_language_code, unit_class_code) VALUES ('s', 'second', 'second', 'en', 'TD');
INSERT INTO public.unit (code, description, name, unit_class_language_code, unit_class_code) VALUES ('A', 'ampere', 'ampere', 'en', 'I');
INSERT INTO public.unit (code, description, name, unit_class_language_code, unit_class_code) VALUES ('K', 'kelvin', 'kelvin', 'en', 'Θ');
INSERT INTO public.unit (code, description, name, unit_class_language_code, unit_class_code) VALUES ('mol', 'mole', 'mole', 'en', 'N');
INSERT INTO public.unit (code, description, name, unit_class_language_code, unit_class_code) VALUES ('cd', 'candela', 'candela', 'en', 'J');

INSERT INTO public.cpv(code, parent, children, name, language_code)

