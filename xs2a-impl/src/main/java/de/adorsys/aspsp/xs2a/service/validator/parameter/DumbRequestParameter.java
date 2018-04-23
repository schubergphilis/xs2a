package de.adorsys.aspsp.xs2a.service.validator.parameter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DumbRequestParameter extends CommonRequestParameter {
}
