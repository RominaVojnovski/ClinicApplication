CREATE TABLE Patient (ID BIGINT NOT NULL, BIRTHDATE DATE, NAME VARCHAR(255), PATIENTID BIGINT, PRIMARY KEY (ID))
CREATE TABLE Treatment (ID BIGINT NOT NULL, DIAGNOSIS VARCHAR(255), patient_fk BIGINT, PRIMARY KEY (ID))
ALTER TABLE Treatment ADD CONSTRAINT FK_Treatment_patient_fk FOREIGN KEY (patient_fk) REFERENCES Patient (ID)
