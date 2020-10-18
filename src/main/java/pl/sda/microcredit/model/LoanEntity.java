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

    private void extend(Integer extendTerm){
        endDate = endDate.plusMonths(extendTerm);
    }

}
