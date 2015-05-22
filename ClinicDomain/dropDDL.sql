ALTER TABLE Treatment DROP CONSTRAINT FK_Treatment_patient_fk
ALTER TABLE Treatment DROP CONSTRAINT FK_Treatment_provider_fk
ALTER TABLE DRUGTREATMENT DROP CONSTRAINT FK_DRUGTREATMENT_ID
ALTER TABLE RADIOLOGYTREATMENT DROP CONSTRAINT FK_RADIOLOGYTREATMENT_ID
ALTER TABLE SURGERYTREATMENT DROP CONSTRAINT FK_SURGERYTREATMENT_ID
DROP TABLE Patient CASCADE
DROP TABLE Treatment CASCADE
DROP TABLE Provider CASCADE
DROP TABLE DRUGTREATMENT CASCADE
DROP TABLE RADIOLOGYTREATMENT CASCADE
DROP TABLE SURGERYTREATMENT CASCADE
