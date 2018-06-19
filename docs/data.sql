INSERT INTO language (code, name, description) VALUES ('ro', 'Romanian', '[mo] for Moldavian has been withdrawn, recommending [ro] also for Moldavian');
INSERT INTO language (code, name, description) VALUES ('en', 'English', '');

INSERT INTO cpv (code, name, level, parent, language_code) VALUES ('03000000-1', 'Agricultural, farming, fishing, forestry and related products', 1, '', 'en');
INSERT INTO cpv (code, name, level, parent, language_code) VALUES ('03100000-2', 'Agricultural and horticultural products', 2, '03000000-1', 'en');
INSERT INTO cpv (code, name, level, parent, language_code) VALUES ('03110000-5', 'Crops, products of market gardening and horticulture', 3, '03100000-2', 'en');
INSERT INTO cpv (code, name, level, parent, language_code) VALUES ('03111000-2', 'Seeds', 4, '03110000-5', 'en');
INSERT INTO cpv (code, name, level, parent, language_code) VALUES ('03111100-3', 'Soya beans', 4, '03110000-5', 'en');
INSERT INTO cpv (code, name, level, parent, language_code) VALUES ('03111200-4', 'Peanuts', 4, '03110000-5', 'en');
INSERT INTO cpv (code, name, level, parent, language_code) VALUES ('03111300-5', 'Sunflower seeds', 4, '03110000-5', 'en');
INSERT INTO cpv (code, name, level, parent, language_code) VALUES ('03111400-6', 'Cotton seeds', 4, '03110000-5', 'en');
INSERT INTO cpv (code, name, level, parent, language_code) VALUES ('03111500-7', 'Sesame seeds', 4, '03110000-5', 'en');
INSERT INTO cpv (code, name, level, parent, language_code) VALUES ('03111600-8', 'Mustard seeds', 4, '03110000-5', 'en');
INSERT INTO cpv (code, name, level, parent, language_code) VALUES ('03111700-9', 'Vegetable seeds', 4, '03110000-5', 'en');
INSERT INTO cpv (code, name, level, parent, language_code) VALUES ('03111800-0', 'Fruit seeds', 4, '03110000-5', 'en');
INSERT INTO cpv (code, name, level, parent, language_code) VALUES ('03111900-1', 'Flower seeds', 4, '03110000-5', 'en');
INSERT INTO cpv (code, name, level, parent, language_code) VALUES ('03112000-9', 'Unmanufactured tobacco', 4, '03110000-5', 'en');
INSERT INTO cpv (code, name, level, parent, language_code) VALUES ('03113000-6', 'Plants used for sugar manufacturing', 4, '03110000-5', 'en');
INSERT INTO cpv (code, name, level, parent, language_code) VALUES ('03113100-7', 'Sugar beet', 4, '03110000-5', 'en');
INSERT INTO cpv (code, name, level, parent, language_code) VALUES ('03113200-8', 'Sugar cane', 4, '03110000-5', 'en');


INSERT INTO cpvs (code, name, level, parent, language_code) VALUES ('030-1', 'volume', 1, '', 'en');
INSERT INTO cpvs (code, name, level, parent, language_code) VALUES ('030-2', 'liter', 2, '030-1', 'en');
INSERT INTO cpvs (code, name, level, parent, language_code) VALUES ('030-3', 'cbm', 2, '030-1', 'en');

INSERT INTO country (code, name, description, def, language_code) VALUES ('MD', 'MOLDOVA', 'MOLDOVA (REPUBLIC OF)', true, 'en');
INSERT INTO country (code, name, description, def, language_code) VALUES ('UA', 'UKRAINE', 'UKRAINE', false , 'en');

INSERT INTO currency(code, def, name, language_code) values ('MDL', true, 'MDL', 'en');
INSERT INTO currency(code, def, name, language_code) values ('EUR', false, 'EUR', 'en');

INSERT INTO currency_country (currency_code, country_code) VALUES ('MDL', 'MD');
INSERT INTO currency_country (currency_code, country_code) VALUES ('EUR', 'MD');