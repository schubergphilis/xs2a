CREATE SCHEMA IF NOT EXISTS consent;

CREATE TABLE consent.ais_consent (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  iban TEXT
);
