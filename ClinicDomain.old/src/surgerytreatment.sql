--<ScriptOptions statementTerminator=";"/>

CREATE TABLE surgerytreatment (
		id INT8 NOT NULL,
		surgerydate DATE
	);

CREATE UNIQUE INDEX surgerytreatment_pkey ON surgerytreatment (id ASC);

ALTER TABLE surgerytreatment ADD CONSTRAINT surgerytreatment_pkey PRIMARY KEY (id);

ALTER TABLE surgerytreatment ADD CONSTRAINT fk_surgerytreatment_id FOREIGN KEY (id)
	REFERENCES treatment (id);

