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

INSERT INTO cpvs (id, code, name, level, parent, language_id) VALUES ('EN_030-1','030-1', 'volume', 1, '', 'EN');
INSERT INTO cpvs (id, code, name, level, parent, language_id) VALUES ('EN_030-2','030-2', 'liter', 2, 'EN_030-1', 'EN');
INSERT INTO cpvs (id, code, name, level, parent, language_id) VALUES ('EN_030-3','030-3', 'cbm', 2, 'EN_030-1', 'EN');

INSERT INTO country (id, code, name, description, def, language_id) VALUES ('EN_MD', 'MD', 'MOLDOVA', 'MOLDOVA (REPUBLIC OF)', true, 'EN');
INSERT INTO country (id, code, name, description, def, language_id) VALUES ('EN_UA', 'UA', 'UKRAINE', 'UKRAINE', false , 'EN');

INSERT INTO region (id, code, name, description, country_id) VALUES ('EN_MD_Transnistria', 'MD_Transnistria', 'Transnistria', 'Transnistria', 'EN_MD');
INSERT INTO region (id, code, name, description, country_id) VALUES ('EN_MD_Gagauzia', 'MD_Gagauzia', 'Gagauzia', 'Gagauzia', 'EN_MD');
INSERT INTO region (id, code, name, description, country_id) VALUES ('EN_UA_Kiev', 'UA_Kiev', 'Kiev', 'Kiev', 'EN_UA');
INSERT INTO region (id, code, name, description, country_id) VALUES ('EN_UA_Kiev_Oblast', 'UA_Kiev_Oblast ', 'Kiev Oblast', 'Kiev Oblast', 'EN_UA');

INSERT INTO currency(id, code, def, name, language_id) values ('EN_MDL', 'MDL', true, 'MDL', 'EN');
INSERT INTO currency(id, code, def, name, language_id) values ('EN_EUR', 'EUR', false, 'EUR', 'EN');

INSERT INTO currency_country(currency_id, country_id) VALUES ('EN_MDL', 'EN_MD');
INSERT INTO currency_country(currency_id, country_id) VALUES ('EN_EUR', 'EN_MD');

INSERT INTO document_type (id, code, description, name, language_id) VALUES ('EN_TENDER_NOTICE', 'TENDER_NOTICE', 'tenderNotice', 'tenderNotice', 'EN');
INSERT INTO document_type (id, code, description, name, language_id) VALUES ('EN_AWARD_NOTICE', 'AWARD_NOTICE', 'awardNotice', 'awardNotice', 'EN');
INSERT INTO document_type (id, code, description, name, language_id) VALUES ('EN_CONTRACT_NOTICE', 'CONTRACT_NOTICE', 'contractNotice', 'contractNotice', 'EN');

INSERT INTO entity_kind (id, code, description, name) VALUES ('TENDER', 'TENDER', 'tender', 'tender');
INSERT INTO entity_kind (id, code, description, name) VALUES ('BID', 'BID', 'bid', 'bid');
INSERT INTO entity_kind (id, code, description, name) VALUES ('AWARD', 'AWARD', 'award', 'award');
INSERT INTO entity_kind (id, code, description, name) VALUES ('CONTRACT', 'CONTRACT', 'contract', 'contract');

INSERT INTO document_type_entity_kind (document_type_id, entity_kind_id) VALUES ('EN_TENDER_NOTICE', 'TENDER');
INSERT INTO document_type_entity_kind (document_type_id, entity_kind_id) VALUES ('EN_AWARD_NOTICE', 'AWARD');
INSERT INTO document_type_entity_kind (document_type_id, entity_kind_id) VALUES ('EN_CONTRACT_NOTICE', 'CONTRACT');

INSERT INTO registration_scheme(id, code, description, name, country_id) VALUES ('EN_MD-GR', 'MD-GR', 'MD-GR', 'MD-GR', 'EN_MD');
INSERT INTO registration_scheme(id, code, description, name, country_id) VALUES ('EN_UA-GR', 'UA-EDR', 'UA-EDR', 'UA-EDR', 'EN_UA');

INSERT INTO unit_class (id, code, description, name) VALUES ('L', 'L','length','length');
INSERT INTO unit_class (id, code, description, name) VALUES ('M', 'M','mass','mass');
INSERT INTO unit_class (id, code, description, name) VALUES ('T', 'T','time','time');
INSERT INTO unit_class (id, code, description, name) VALUES ('I', 'I','electric current','electric current');
INSERT INTO unit_class (id, code, description, name) VALUES ('Θ', 'Θ','thermodynamic temperature','thermodynamic temperature');
INSERT INTO unit_class (id, code, description, name) VALUES ('N', 'N','amount of substance','amount of substance');
INSERT INTO unit_class (id, code, description, name) VALUES ('J', 'J','luminous intensity','luminous intensity');

INSERT INTO unit(id, code, description, name, language_id, unit_class_id) VALUES ('EN_m','m','metre','metre','EN','L');
INSERT INTO unit(id, code, description, name, language_id, unit_class_id) VALUES ('EN_kg','kg','kilogram','kilogram','EN','M');
INSERT INTO unit(id, code, description, name, language_id, unit_class_id) VALUES ('EN_s','s','second','second','EN','T');
INSERT INTO unit(id, code, description, name, language_id, unit_class_id) VALUES ('EN_A','A','ampere','ampere','EN','I');
INSERT INTO unit(id, code, description, name, language_id, unit_class_id) VALUES ('EN_K','K','kelvin','kelvin','EN','Θ');
INSERT INTO unit(id, code, description, name, language_id, unit_class_id) VALUES ('EN_mol','mol','mole','mole','EN','N');
INSERT INTO unit(id, code, description, name, language_id, unit_class_id) VALUES ('EN_cd','cd','candela','candela','EN','J');

INSERT INTO holidays (id, code, holiday_date, description, name, country_id) VALUES ('EN_UA_01', '01', '2019-01-01 00:00:00', 'New Year', 'New Year', 'EN_UA');