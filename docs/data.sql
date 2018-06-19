INSERT INTO language (code, name, description) VALUES ('RO', 'Romanian', '[mo] for Moldavian has been withdrawn, recommending [ro] also for Moldavian');
INSERT INTO language (code, name, description) VALUES ('EN', 'English', '');

INSERT INTO cpv (code, name, level, parent, language_code) VALUES ('03000000-1', 'Agricultural, farming, fishing, forestry and related products', 1, '', 'EN');
INSERT INTO cpv (code, name, level, parent, language_code) VALUES ('03100000-2', 'Agricultural and horticultural products', 2, '03000000-1', 'EN');
INSERT INTO cpv (code, name, level, parent, language_code) VALUES ('03110000-5', 'Crops, products of market gardening and horticulture', 3, '03100000-2', 'EN');
INSERT INTO cpv (code, name, level, parent, language_code) VALUES ('03111000-2', 'Seeds', 4, '03110000-5', 'EN');
INSERT INTO cpv (code, name, level, parent, language_code) VALUES ('03111100-3', 'Soya beans', 4, '03110000-5', 'EN');
INSERT INTO cpv (code, name, level, parent, language_code) VALUES ('03111200-4', 'Peanuts', 4, '03110000-5', 'EN');
INSERT INTO cpv (code, name, level, parent, language_code) VALUES ('03111300-5', 'Sunflower seeds', 4, '03110000-5', 'EN');
INSERT INTO cpv (code, name, level, parent, language_code) VALUES ('03111400-6', 'Cotton seeds', 4, '03110000-5', 'EN');
INSERT INTO cpv (code, name, level, parent, language_code) VALUES ('03111500-7', 'Sesame seeds', 4, '03110000-5', 'EN');
INSERT INTO cpv (code, name, level, parent, language_code) VALUES ('03111600-8', 'Mustard seeds', 4, '03110000-5', 'EN');
INSERT INTO cpv (code, name, level, parent, language_code) VALUES ('03111700-9', 'Vegetable seeds', 4, '03110000-5', 'EN');
INSERT INTO cpv (code, name, level, parent, language_code) VALUES ('03111800-0', 'Fruit seeds', 4, '03110000-5', 'EN');
INSERT INTO cpv (code, name, level, parent, language_code) VALUES ('03111900-1', 'Flower seeds', 4, '03110000-5', 'EN');
INSERT INTO cpv (code, name, level, parent, language_code) VALUES ('03112000-9', 'Unmanufactured tobacco', 4, '03110000-5', 'EN');
INSERT INTO cpv (code, name, level, parent, language_code) VALUES ('03113000-6', 'Plants used for sugar manufacturing', 4, '03110000-5', 'EN');
INSERT INTO cpv (code, name, level, parent, language_code) VALUES ('03113100-7', 'Sugar beet', 4, '03110000-5', 'EN');
INSERT INTO cpv (code, name, level, parent, language_code) VALUES ('03113200-8', 'Sugar cane', 4, '03110000-5', 'EN');

INSERT INTO cpvs (code, name, level, parent, language_code) VALUES ('030-1', 'volume', 1, '', 'EN');
INSERT INTO cpvs (code, name, level, parent, language_code) VALUES ('030-2', 'liter', 2, '030-1', 'EN');
INSERT INTO cpvs (code, name, level, parent, language_code) VALUES ('030-3', 'cbm', 2, '030-1', 'EN');

INSERT INTO country (code, name, description, def, language_code) VALUES ('MD', 'MOLDOVA', 'MOLDOVA (REPUBLIC OF)', true, 'EN');
INSERT INTO country (code, name, description, def, language_code) VALUES ('UA', 'UKRAINE', 'UKRAINE', false , 'EN');

INSERT INTO region (code, name, description, language_code, country_code) VALUES ('MD_Transnistria', 'Transnistria', 'Transnistria', 'EN', 'MD');
INSERT INTO region (code, name, description, language_code, country_code) VALUES ('MD_Gagauzia', 'Gagauzia', 'Gagauzia', 'EN', 'MD');
INSERT INTO region (code, name, description, language_code, country_code) VALUES ('UA_Kiev', 'Kiev', 'Kiev', 'EN', 'UA');
INSERT INTO region (code, name, description, language_code, country_code) VALUES ('UA_Kiev_Oblast ', 'Kiev Oblast', 'Kiev Oblast', 'EN', 'UA');

INSERT INTO currency(code, def, name, language_code) values ('MDL', true, 'MDL', 'EN');
INSERT INTO currency(code, def, name, language_code) values ('EUR', false, 'EUR', 'EN');

INSERT INTO currency_country (currency_code, country_code) VALUES ('MDL', 'MD');
INSERT INTO currency_country (currency_code, country_code) VALUES ('EUR', 'MD');

INSERT INTO document_type (code, description, name, language_code) VALUES ('TENDER_NOTICE', 'tenderNotice', 'tenderNotice', 'EN');
INSERT INTO document_type (code, description, name, language_code) VALUES ('AWARD_NOTICE', 'awardNotice', 'awardNotice', 'EN');
INSERT INTO document_type (code, description, name, language_code) VALUES ('CONTRACT_NOTICE', 'contractNotice', 'contractNotice', 'EN');
INSERT INTO document_type (code, description, name, language_code) VALUES ('COMPLETION_CERTIFICATE', 'completionCertificate', 'completionCertificate', 'EN');
INSERT INTO document_type (code, description, name, language_code) VALUES ('PROCUREMENT_PLAN', 'procurementPlan', 'procurementPlan', 'EN');
INSERT INTO document_type (code, description, name, language_code) VALUES ('BIDDING_DOCUMENTS', 'biddingDocuments', 'biddingDocuments', 'EN');
INSERT INTO document_type (code, description, name, language_code) VALUES ('TECHNICAL_SPECIFICATIONS', 'technicalSpecifications', 'technicalSpecifications', 'EN');
INSERT INTO document_type (code, description, name, language_code) VALUES ('EVALUATION_CRITERIA', 'evaluationCriteria', 'evaluationCriteria', 'EN');
INSERT INTO document_type (code, description, name, language_code) VALUES ('EVALUATION_REPORTS', 'evaluationReports', 'evaluationReports', 'EN');
INSERT INTO document_type (code, description, name, language_code) VALUES ('CONTRACT_DRAFT', 'contractDraft', 'contractDraft', 'EN');
INSERT INTO document_type (code, description, name, language_code) VALUES ('CONTRACT_SIGNED', 'contractSigned', 'contractSigned', 'EN');
INSERT INTO document_type (code, description, name, language_code) VALUES ('CONTRACT_ARRANGEMENTS', 'contractArrangements', 'contractArrangements', 'EN');
INSERT INTO document_type (code, description, name, language_code) VALUES ('CONTRACT_SCHEDULE', 'contractSchedule', 'contractSchedule', 'EN');

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

INSERT INTO registration_scheme(code, description, name, country_code, language_code) VALUES ('MD-GR', 'MD-GR', 'MD-GR', 'MD', 'EN');
INSERT INTO registration_scheme(code, description, name, country_code, language_code) VALUES ('UA-EDR', 'UA-EDR', 'UA-EDR', 'UA', 'EN');

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

INSERT INTO holidays (code, holiday_date, description, name, country_code, language_code) VALUES ('01', '2019-01-01 00:00:00', 'New Year', 'New Year', 'UA', 'EN');