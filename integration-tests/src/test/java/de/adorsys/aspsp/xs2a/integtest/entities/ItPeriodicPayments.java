package de.adorsys.aspsp.xs2a.integtest.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.adorsys.aspsp.xs2a.domain.code.FrequencyCode;
import de.adorsys.aspsp.xs2a.domain.pis.SinglePayments;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;
import java.util.Optional;


@Data
@ApiModel(description = "Periodic Payment Initialisation Request", value = "Periodic Payment")
public class ItPeriodicPayments extends SinglePayments {

    @ApiModelProperty(name = "startDate", required = true, example = "2017-03-03")
    private LocalDate startDate;

    @ApiModelProperty(name = "executionRule", required = false, example = "preceeding")
    private String executionRule;

    @ApiModelProperty(name = "endDate", required = false, example = "2018-03-03")
    private LocalDate endDate;

    @ApiModelProperty(name = "frequency", required = true, example = "ANNUAL")
    private String frequency;

    @ApiModelProperty(name = "dayOfExecution", required = false, example = "14")
    @Max(31)
    @Min(1)
    private int dayOfExecution; //Day here max 31

    @JsonIgnore
    public boolean isValidDate() {
        return isValidDated() && isValidStartDate() //TODO Should be removed with https://git.adorsys.de/adorsys/xs2a/aspsp-xs2a/issues/167
                   &&
                   Optional.ofNullable(this.endDate)
                       .map(d -> d.isAfter(this.startDate))
                       .orElse(true);

    }

    @JsonIgnore
    private boolean isValidStartDate() { //TODO Should be removed with https://git.adorsys.de/adorsys/xs2a/aspsp-xs2a/issues/167
        return this.startDate.isEqual(ChronoLocalDate.from(LocalDate.now()))
                   || this.startDate.isAfter(ChronoLocalDate.from(LocalDate.now()));
    }

}
