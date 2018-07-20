CREATE SEQUENCE consent.ais_account_id_seq;
CREATE SEQUENCE consent.ais_consent_id_seq;

CREATE TABLE IF NOT EXISTS consent.ais_consent(
    id bigint NOT NULL,
    combined_service_indicator boolean NOT NULL,
    consent_status character varying(25) NOT NULL,
    consent_type character varying(5) NOT NULL,
    expected_frequency_per_day integer NOT NULL,
    expire_date bytea,
    external_id character varying(40) NOT NULL,
    psu_id character varying(16),
    recurring_indicator boolean NOT NULL,
    request_date bytea NOT NULL,
    tpp_frequency_per_day integer NOT NULL,
    tpp_id character varying(16),
    tpp_redirect_preferred boolean NOT NULL,
    usage_counter integer NOT NULL,
    CONSTRAINT ais_consent_pkey PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS consent.ais_account(
    id bigint NOT NULL,
    iban character varying(34)  NOT NULL,
    consent_id bigint NOT NULL,
    CONSTRAINT ais_account_pkey PRIMARY KEY (id),
    CONSTRAINT consent_id_fkey FOREIGN KEY (consent_id) REFERENCES ais_consent (id)
);

CREATE TABLE IF NOT EXISTS consent.ais_account_access(
    account_id bigint NOT NULL,
    currency character varying(5) NOT NULL,
    type_access character varying(15) NOT NULL,
    CONSTRAINT ais_account_access_pkey PRIMARY KEY (account_id, currency, type_access),
    CONSTRAINT account_id_fkey FOREIGN KEY (account_id) REFERENCES ais_account (id)
);
