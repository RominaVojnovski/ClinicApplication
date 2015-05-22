--<ScriptOptions statementTerminator=";"/>

CREATE TABLE radiologytreatment (
		id INT8 NOT NULL,
		dates BYTEA(2147483647)
	);

CREATE UNIQUE INDEX radiologytreatment_pkey ON radiologytreatment (id ASC);

ALTER TABLE radiologytreatment ADD CONSTRAINT radiologytreatment_pkey PRIMARY KEY (id);

ALTER TABLE radiologytreatment ADD CONSTRAINT fk_radiologytreatment_id FOREIGN KEY (id)
	REFERENCES treatment (id);

