package de.adorsys.aspsp.xs2a.spi.domain;

import de.adorsys.aspsp.xs2a.spi.domain.consent.AspspConsentData;
import lombok.Value;

@Value
public class SpiResponse<T> {
    private T payload;
    private AspspConsentData aspspConsentData;
    private ErrorStatus status;
    private String message;

    public SpiResponse(T payload, AspspConsentData aspspConsentData, ErrorStatus status, String message) {
        this.payload = payload;
        this.aspspConsentData = aspspConsentData;
        this.status = status;
        this.message = message;
    }

    public SpiResponse(T payload, AspspConsentData aspspConsentData) {
        this.payload = payload;
        this.aspspConsentData = aspspConsentData;
        this.status = null;
        this.message = null;
    }

    public boolean hasError(){
        return status==null;
    }

    public enum ErrorStatus{
        NOT_FOUND,
        BAD_REQUEST,
        REJECTED
    }
}
