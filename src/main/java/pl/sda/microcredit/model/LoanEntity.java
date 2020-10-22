package pl.sda.microcredit.model;

import com.sun.istack.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
@Table(name = "Loan")
public class LoanEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private BigDecimal amount;

    @NotNull
    private BigDecimal amountInterest;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    private Long extendTerm;

    @NotNull
    private Long period;

    @NotNull
    private LocalDate createDate;

    public void extend(Long extendTerm) {
        if (this.extendTerm == null) {
            this.extendTerm = extendTerm;
        } else {
            this.extendTerm += extendTerm;
        }
        this.endDate = this.endDate.plusMonths(extendTerm);
    }

    public LoanView toView() {
        return LoanView.builder()
                .id(id)
                .amount(amount)
                .amountInterest(amountInterest)
                .startDate(startDate)
                .endDate(endDate)
                .extendTerm(extendTerm)
                .period(period)
                .createDate(createDate)
                .build();
    }

}
