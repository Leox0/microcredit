package pl.sda.microcredit.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
@AllArgsConstructor
@Getter
public class LoanView {

    private Long id;

    private BigDecimal amount;

    private BigDecimal amountInterest;

    private LocalDate startDate;

    private LocalDate endDate;

    private Long extendTerm;

    private Long period;

    private LocalDate createDate;

}
