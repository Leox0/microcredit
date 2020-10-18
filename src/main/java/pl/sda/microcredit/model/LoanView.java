package pl.sda.microcredit.model;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
public class LoanView {

    private BigDecimal amount;

    private BigDecimal amountInterest;

    private LocalDate startDate;

    private LocalDate endDate;

    private Long extendTerm;

    private Long period;

    private LocalDate createDate;

}
