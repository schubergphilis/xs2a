package de.adorsys.aspsp.xs2a.component;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class CustomDeserializer extends JsonDeserializer<Instant> {
    
    @Override
    public Instant deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        LocalDate localDate = LocalDate.parse(p.getText());
        LocalDateTime localDateTime = localDate.atStartOfDay();
        return localDateTime.toInstant(ZoneOffset.UTC);
    }
}
