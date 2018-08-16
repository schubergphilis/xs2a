package de.adorsys.aspsp.xs2a.integtest.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import de.adorsys.aspsp.xs2a.domain.TransactionStatus;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ITMessageError {

    private TransactionStatus transactionStatus;
    private List<ITTppMessageInformation> tppMessages;
}
