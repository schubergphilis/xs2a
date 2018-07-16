package de.adorsys.aspsp.xs2a.stepdefinitions;

import lombok.Builder;
import lombok.Data;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

@Component
@Data
@Builder
public class Context {
    String scaApproach;
    String paymentProduct;
    String accessToken;
}
