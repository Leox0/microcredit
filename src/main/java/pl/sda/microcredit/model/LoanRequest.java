package pl.sda.microcredit.model;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class LoanRequest {

    @NotNull
    private BigDecimal amount;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private Long period;

}
