CREATE SEQUENCE pis_consent_id_seq;

CREATE TABLE IF NOT EXISTS pis_consent(
    id bigint NOT NULL,
    consent_status character varying(25) NOT NULL,
    consent_type character varying(5) NOT NULL,
    external_id character varying(40) NOT NULL,
    pis_consent_type character varying(16) NOT NULL,
    CONSTRAINT pis_consent_pkey PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS pis_payments (
	pis_consent_id int8 NOT NULL,
	payment_ids varchar(255) NULL,
	CONSTRAINT pis_consent_fkey FOREIGN KEY (pis_consent_id) REFERENCES pis_consent (id)
);

