INSERT INTO language (id, code, name, description) VALUES ('RO', 'RO', 'Romanian', '[mo] for Moldavian has been withdrawn, recommending [ro] also for Moldavian');
INSERT INTO language (id, code, name, description) VALUES ('EN', 'EN', 'English', 'English');

INSERT INTO cpv (id, code, name, level, parent, language_id) VALUES ('EN_03000000-1', '03000000-1', 'Agricultural, farming, fishing, forestry and related products', 1, '', 'EN');
INSERT INTO cpv (id, code, name, level, parent, language_id) VALUES ('EN_03100000-2', '03100000-2', 'Agricultural and horticultural products', 2, '03000000-1', 'EN');
INSERT INTO cpv (id, code, name, level, parent, language_id) VALUES ('EN_03110000-5', '03110000-5', 'Crops, products of market gardening and horticulture', 3, '03100000-2', 'EN');
INSERT INTO cpv (id, code, name, level, parent, language_id) VALUES ('EN_03111000-2', '03111000-2', 'Seeds', 4, '03110000-5', 'EN');
INSERT INTO cpv (id, code, name, level, parent, language_id) VALUES ('EN_03111100-3', '03111100-3', 'Soya beans', 4, '03110000-5', 'EN');

INSERT INTO cpvs (id, code, name, level, parent, language_id) VALUES ('EN_030-1','030-1', 'volume', 1, '', 'EN');
INSERT INTO cpvs (id, code, name, level, parent, language_id) VALUES ('EN_030-2','030-2', 'liter', 2, '030-1', 'EN');
INSERT INTO cpvs (id, code, name, level, parent, language_id) VALUES ('EN_030-3','030-3', 'cbm', 2, '030-1', 'EN');

INSERT INTO country (id, code, name, description, def, language_id) VALUES ('EN_MD', 'MD', 'MOLDOVA', 'MOLDOVA (REPUBLIC OF)', true, 'EN');
INSERT INTO country (id, code, name, description, def, language_id) VALUES ('EN_UA', 'UA', 'UKRAINE', 'UKRAINE', false , 'EN');

INSERT INTO region (id, code, name, description, country_id) VALUES ('EN_MD_Transnistria', 'MD_Transnistria', 'Transnistria', 'Transnistria', 'EN_MD');
INSERT INTO region (id, code, name, description, country_id) VALUES ('EN_MD_Gagauzia', 'MD_Gagauzia', 'Gagauzia', 'Gagauzia', 'EN_MD');
INSERT INTO region (id, code, name, description, country_id) VALUES ('EN_UA_Kiev', 'UA_Kiev', 'Kiev', 'Kiev', 'EN_UA');
INSERT INTO region (id, code, name, description, country_id) VALUES ('EN_UA_Kiev_Oblast', 'UA_Kiev_Oblast ', 'Kiev Oblast', 'Kiev Oblast', 'EN_UA');

INSERT INTO currency(id, code, def, name, language_id) values ('MDL', true, 'MDL', 'EN');
INSERT INTO currency(id, code, def, name, language_id) values ('EUR', false, 'EUR', 'EN');

INSERT INTO currency_country(currency_id, country_id) VALUES ('MDL', 'MD');
INSERT INTO currency_country(currency_id, country_id) VALUES ('EUR', 'MD');

INSERT INTO document_type (code, description, name, language_id) VALUES ('TENDER_NOTICE', 'tenderNotice', 'tenderNotice', 'EN');
INSERT INTO document_type (code, description, name, language_id) VALUES ('AWARD_NOTICE', 'awardNotice', 'awardNotice', 'EN');
INSERT INTO document_type (code, description, name, language_id) VALUES ('CONTRACT_NOTICE', 'contractNotice', 'contractNotice', 'EN');
INSERT INTO document_type (code, description, name, language_id) VALUES ('COMPLETION_CERTIFICATE', 'completionCertificate', 'completionCertificate', 'EN');
INSERT INTO document_type (code, description, name, language_id) VALUES ('PROCUREMENT_PLAN', 'procurementPlan', 'procurementPlan', 'EN');
INSERT INTO document_type (code, description, name, language_id) VALUES ('BIDDING_DOCUMENTS', 'biddingDocuments', 'biddingDocuments', 'EN');
INSERT INTO document_type (code, description, name, language_id) VALUES ('TECHNICAL_SPECIFICATIONS', 'technicalSpecifications', 'technicalSpecifications', 'EN');
INSERT INTO document_type (code, description, name, language_id) VALUES ('EVALUATION_CRITERIA', 'evaluationCriteria', 'evaluationCriteria', 'EN');
INSERT INTO document_type (code, description, name, language_id) VALUES ('EVALUATION_REPORTS', 'evaluationReports', 'evaluationReports', 'EN');
INSERT INTO document_type (code, description, name, language_id) VALUES ('CONTRACT_DRAFT', 'contractDraft', 'contractDraft', 'EN');
INSERT INTO document_type (code, description, name, language_id) VALUES ('CONTRACT_SIGNED', 'contractSigned', 'contractSigned', 'EN');
INSERT INTO document_type (code, description, name, language_id) VALUES ('CONTRACT_ARRANGEMENTS', 'contractArrangements', 'contractArrangements', 'EN');
INSERT INTO document_type (code, description, name, language_id) VALUES ('CONTRACT_SCHEDULE', 'contractSchedule', 'contractSchedule', 'EN');

INSERT INTO entity_kind (code, description, name) VALUES ('TENDER', 'tender', 'tender');
INSERT INTO entity_kind (code, description, name) VALUES ('BID', 'bid', 'bid');
INSERT INTO entity_kind (code, description, name) VALUES ('AWARD', 'award', 'award');
INSERT INTO entity_kind (code, description, name) VALUES ('CONTRACT', 'contract', 'contract');

INSERT INTO document_type_entity_kind (document_type_code, entity_kind_code) VALUES ('TENDER_NOTICE', 'TENDER');
INSERT INTO document_type_entity_kind (document_type_code, entity_kind_code) VALUES ('BIDDING_DOCUMENTS', 'BID');
INSERT INTO document_type_entity_kind (document_type_code, entity_kind_code) VALUES ('AWARD_NOTICE', 'AWARD');
INSERT INTO document_type_entity_kind (document_type_code, entity_kind_code) VALUES ('CONTRACT_DRAFT', 'CONTRACT');
INSERT INTO document_type_entity_kind (document_type_code, entity_kind_code) VALUES ('CONTRACT_SIGNED', 'CONTRACT');
INSERT INTO document_type_entity_kind (document_type_code, entity_kind_code) VALUES ('CONTRACT_ARRANGEMENTS', 'CONTRACT');
INSERT INTO document_type_entity_kind (document_type_code, entity_kind_code) VALUES ('CONTRACT_SCHEDULE', 'CONTRACT');

INSERT INTO registration_scheme(code, description, name, country_id, language_id) VALUES ('MD-GR', 'MD-GR', 'MD-GR', 'MD', 'EN');
INSERT INTO registration_scheme(code, description, name, country_id, language_id) VALUES ('UA-EDR', 'UA-EDR', 'UA-EDR', 'UA', 'EN');

INSERT INTO unit_class (code, description, name) VALUES ('L','length','length');
INSERT INTO unit_class (code, description, name) VALUES ('M','mass','mass');
INSERT INTO unit_class (code, description, name) VALUES ('T','time','time');
INSERT INTO unit_class (code, description, name) VALUES ('I','electric current','electric current');
INSERT INTO unit_class (code, description, name) VALUES ('Θ','thermodynamic temperature','thermodynamic temperature');
INSERT INTO unit_class (code, description, name) VALUES ('N','amount of substance','amount of substance');
INSERT INTO unit_class (code, description, name) VALUES ('J','luminous intensity','luminous intensity');

INSERT INTO unit(code, description, name, language_id, unit_class_code) VALUES ('m','metre','metre','EN','L');
INSERT INTO unit(code, description, name, language_id, unit_class_code) VALUES ('kg','kilogram','kilogram','EN','M');
INSERT INTO unit(code, description, name, language_id, unit_class_code) VALUES ('s','second','second','EN','T');
INSERT INTO unit(code, description, name, language_id, unit_class_code) VALUES ('A','ampere','ampere','EN','I');
INSERT INTO unit(code, description, name, language_id, unit_class_code) VALUES ('K','kelvin','kelvin','EN','Θ');
INSERT INTO unit(code, description, name, language_id, unit_class_code) VALUES ('mol','mole','mole','EN','N');
INSERT INTO unit(code, description, name, language_id, unit_class_code) VALUES ('cd','candela','candela','EN','J');

INSERT INTO holidays (code, holiday_date, description, name, country_id, language_id) VALUES ('01', '2019-01-01 00:00:00', 'New Year', 'New Year', 'UA', 'EN');