CREATE SEQUENCE IF NOT EXISTS ais_account_id_seq;
CREATE SEQUENCE IF NOT EXISTS ais_consent_id_seq;
CREATE SEQUENCE IF NOT EXISTS ais_consent_action_id_seq;
CREATE SEQUENCE IF NOT EXISTS pis_consent_id_seq;

CREATE TABLE IF NOT EXISTS ais_consent (
    id bigint NOT NULL,
    combined_service_indicator boolean NOT NULL,
    consent_status character varying(25) NOT NULL,
    consent_type character varying(5) NOT NULL,
    expected_frequency_per_day integer NOT NULL,
    expire_date bytea,
    external_id character varying(40) NOT NULL,
    last_action_date bytea,
    psu_id character varying(255),
    recurring_indicator boolean NOT NULL,
    request_date_time bytea NOT NULL,
    tpp_frequency_per_day integer NOT NULL,
    tpp_id character varying(16) NOT NULL,
    tpp_redirect_preferred boolean NOT NULL,
    usage_counter integer NOT NULL,
    CONSTRAINT ais_consent_pkey PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS ais_account (
    id bigint NOT NULL,
    iban character varying(34)  NOT NULL,
    consent_id bigint NOT NULL,
    CONSTRAINT ais_account_pkey PRIMARY KEY (id),
    CONSTRAINT consent_id_fkey FOREIGN KEY (consent_id) REFERENCES ais_consent (id)
);

CREATE TABLE IF NOT EXISTS ais_account_access (
    account_id bigint NOT NULL,
    currency character varying(3) NOT NULL,
    type_access character varying(15) NOT NULL,
    CONSTRAINT ais_account_access_pkey PRIMARY KEY (account_id, currency, type_access),
    CONSTRAINT account_id_fkey FOREIGN KEY (account_id) REFERENCES ais_account (id)
);

CREATE TABLE ais_consent_action (
    id bigint NOT NULL,
    action_status character varying(50) NOT NULL,
    request_date bytea NOT NULL,
    requested_consent_id character varying(255) NOT NULL,
    tpp_id character varying(16) NOT NULL,
    CONSTRAINT ais_consent_action_pkey PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS pis_consent (
    id bigint NOT NULL,
    consent_status character varying(25) NOT NULL,
    consent_type character varying(5) NOT NULL,
    external_id character varying(40) NOT NULL,
    pis_consent_type character varying(16) NOT NULL,
    CONSTRAINT pis_consent_pkey PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS pis_payment (
	pis_consent_id int8 NOT NULL,
	payment_id varchar(255) NULL,
	CONSTRAINT pis_consent_fkey FOREIGN KEY (pis_consent_id) REFERENCES pis_consent (id)
);

