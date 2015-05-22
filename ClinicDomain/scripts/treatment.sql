--<ScriptOptions statementTerminator=";"/>

CREATE TABLE treatment (
		id INT8 NOT NULL,
		diagnosis VARCHAR(255),
		patient_fk INT8
	);

CREATE UNIQUE INDEX treatment_pkey ON treatment (id ASC);

ALTER TABLE treatment ADD CONSTRAINT treatment_pkey PRIMARY KEY (id);

ALTER TABLE treatment ADD CONSTRAINT fk_treatment_patient_fk FOREIGN KEY (patient_fk)
	REFERENCES patient (id);

