INSERT INTO language (code, name, description) VALUES ('RO', 'Romanian', '[mo] for Moldavian has been withdrawn, recommending [ro] also for Moldavian');
INSERT INTO language (code, name, description) VALUES ('EN', 'English', 'English');

INSERT INTO cpv (language_code, code, name, level, parent) VALUES ('EN', '03000000-1', 'Agricultural, farming, fishing, forestry and related products', 1, '');
INSERT INTO cpv (language_code, code, name, level, parent) VALUES ('EN', '03100000-2', 'Agricultural and horticultural products', 2, '03000000-1');
INSERT INTO cpv (language_code, code, name, level, parent) VALUES ('EN', '03110000-5', 'Crops, products of market gardening and horticulture', 3, '03100000-2');
INSERT INTO cpv (language_code, code, name, level, parent) VALUES ('EN', '03111000-2', 'Seeds', 4, '03110000-5');
INSERT INTO cpv (language_code, code, name, level, parent) VALUES ('EN', '03111100-3', 'Soya beans', 4, '03110000-5');
INSERT INTO cpv (language_code, code, name, level, parent) VALUES ('RO', '03000000-1', 'Agricultural, farming, fishing forestry and related products', 1, '');
INSERT INTO cpv (language_code, code, name, level, parent) VALUES ('RO', '03100000-2', 'Agricultural and horticultural products', 2, '03000000-1');
INSERT INTO cpv (language_code, code, name, level, parent) VALUES ('RO', '03110000-5', 'Crops, products of market gardening and horticulture', 3, '03100000-2');
INSERT INTO cpv (language_code, code, name, level, parent) VALUES ('RO', '03111000-2', 'Seeds', 4, '03110000-5');
INSERT INTO cpv (language_code, code, name, level, parent) VALUES ('RO', '03111100-3', 'Soya beans', 4, '03110000-5');

INSERT INTO cpvs (language_code, code, name, level, parent) VALUES ('EN','030-1', 'volume', 1, '');
INSERT INTO cpvs (language_code, code, name, level, parent) VALUES ('EN','030-2', 'liter', 2, 'EN_030-1');
INSERT INTO cpvs (language_code, code, name, level, parent) VALUES ('EN','030-3', 'cbm', 2, 'EN_030-1');
INSERT INTO cpvs (language_code, code, name, level, parent) VALUES ('RO','030-1', 'volume', 1, '');
INSERT INTO cpvs (language_code, code, name, level, parent) VALUES ('RO','030-2', 'liter', 2, 'EN_030-1');
INSERT INTO cpvs (language_code, code, name, level, parent) VALUES ('RO','030-3', 'cbm', 2, 'EN_030-1');

INSERT INTO unit_class (code, description, name) VALUES ('L','length','length');
INSERT INTO unit_class (code, description, name) VALUES ('M','mass','mass');
INSERT INTO unit_class (code, description, name) VALUES ('T','time','time');
INSERT INTO unit_class (code, description, name) VALUES ('I','electric current','electric current');
INSERT INTO unit_class (code, description, name) VALUES ('Θ','thermodynamic temperature','thermodynamic temperature');
INSERT INTO unit_class (code, description, name) VALUES ('N','amount of substance','amount of substance');
INSERT INTO unit_class (code, description, name) VALUES ('J','luminous intensity','luminous intensity');

INSERT INTO unit(code, description, name, language_code, unit_class_code) VALUES ('m','metre','metre','EN','L');
INSERT INTO unit(code, description, name, language_code, unit_class_code) VALUES ('kg','kilogram','kilogram','EN','M');
INSERT INTO unit(code, description, name, language_code, unit_class_code) VALUES ('s','second','second','EN','T');
INSERT INTO unit(code, description, name, language_code, unit_class_code) VALUES ('A','ampere','ampere','EN','I');
INSERT INTO unit(code, description, name, language_code, unit_class_code) VALUES ('K','kelvin','kelvin','EN','Θ');
INSERT INTO unit(code, description, name, language_code, unit_class_code) VALUES ('mol','mole','mole','EN','N');
INSERT INTO unit(code, description, name, language_code, unit_class_code) VALUES ('cd','candela','candela','EN','J');

INSERT INTO document_type (code, description, name, language_code) VALUES ('TENDER_NOTICE', 'tenderNotice', 'tenderNotice', 'EN');
INSERT INTO document_type (code, description, name, language_code) VALUES ('AWARD_NOTICE', 'awardNotice', 'awardNotice', 'EN');
INSERT INTO document_type (code, description, name, language_code) VALUES ('CONTRACT_NOTICE', 'contractNotice', 'contractNotice', 'EN');

INSERT INTO entity_kind (code, description, name) VALUES ('TENDER', 'tender', 'tender');
INSERT INTO entity_kind (code, description, name) VALUES ('AWARD', 'award', 'award');
INSERT INTO entity_kind (code, description, name) VALUES ('CONTRACT', 'contract', 'contract');

INSERT INTO document_type_entity_kind (document_type_code, document_type_language_code, entity_kind_code) VALUES ('TENDER_NOTICE', 'EN', 'TENDER');
INSERT INTO document_type_entity_kind (document_type_code, document_type_language_code, entity_kind_code) VALUES ('AWARD_NOTICE', 'EN', 'AWARD');
INSERT INTO document_type_entity_kind (document_type_code, document_type_language_code, entity_kind_code) VALUES ('CONTRACT_NOTICE', 'EN', 'CONTRACT');


INSERT INTO country (code, name, description, def, language_code) VALUES ('MD', 'MOLDOVA', 'MOLDOVA (REPUBLIC OF)', true, 'EN');
INSERT INTO country (code, name, description, def, language_code) VALUES ('UA', 'UKRAINE', 'UKRAINE', false , 'EN');

INSERT INTO currency(code, def, name, language_code) values ('MDL', true, 'MDL', 'EN');
INSERT INTO currency(code, def, name, language_code) values ('EUR', false, 'EUR', 'EN');

INSERT INTO currency_country(currency_code, currency_language_code, country_code, country_language_code) VALUES ('MDL', 'EN', 'MD', 'EN');
INSERT INTO currency_country(currency_code, currency_language_code, country_code, country_language_code) VALUES ('EUR', 'EN', 'MD', 'EN');

INSERT INTO region (id, code, name, description, country_id) VALUES ('EN_MD_Transnistria', 'MD_Transnistria', 'Transnistria', 'Transnistria', 'EN_MD');
INSERT INTO region (id, code, name, description, country_id) VALUES ('EN_MD_Gagauzia', 'MD_Gagauzia', 'Gagauzia', 'Gagauzia', 'EN_MD');
INSERT INTO region (id, code, name, description, country_id) VALUES ('EN_UA_Kiev', 'UA_Kiev', 'Kiev', 'Kiev', 'EN_UA');
INSERT INTO region (id, code, name, description, country_id) VALUES ('EN_UA_Kiev_Oblast', 'UA_Kiev_Oblast ', 'Kiev Oblast', 'Kiev Oblast', 'EN_UA');



INSERT INTO registration_scheme(id, code, description, name, country_id) VALUES ('EN_MD-GR', 'MD-GR', 'MD-GR', 'MD-GR', 'EN_MD');
INSERT INTO registration_scheme(id, code, description, name, country_id) VALUES ('EN_UA-GR', 'UA-EDR', 'UA-EDR', 'UA-EDR', 'EN_UA');

INSERT INTO holidays (id, code, holiday_date, description, name, country_id) VALUES ('EN_UA_01', '01', '2019-01-01 00:00:00', 'New Year', 'New Year', 'EN_UA');