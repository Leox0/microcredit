package pl.sda.microcredit.model;

import com.sun.istack.NotNull;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
public class LoanRequest {

    @NotNull
    private BigDecimal amount;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private Long period;

}
