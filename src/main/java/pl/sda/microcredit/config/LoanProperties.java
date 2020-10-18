package pl.sda.microcredit.config;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@NoArgsConstructor
@Component
@Getter
public class LoanProperties {

    @Value("${properties.maxAmount}")
    private BigDecimal maxAmount;

    @Value("${properties.minAmount}")
    private BigDecimal minAmount;

    @Value("${properties.maxTermMonth}")
    private Long maxTermMonth;

    @Value("${properties.minTermMonth}")
    private Long minTermMonth;

    @Value("${properties.extendMonth}")
    private Long extendMonth;

    @Value("${properties.interest}")
    private Long interest;

}
