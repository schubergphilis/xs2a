package de.adorsys.aspsp.xs2a.integtest.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.adorsys.aspsp.xs2a.domain.pis.SinglePayment;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;
import java.util.Optional;


@Data
public class ITPeriodicPayments extends SinglePayment {

    private LocalDate startDate;

    private String executionRule;

    private LocalDate endDate;

    private String frequency;

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
