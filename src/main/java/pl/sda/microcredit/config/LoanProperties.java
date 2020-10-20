package pl.sda.microcredit.config;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalTime;

@NoArgsConstructor
@Component
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

    @Value("${properties.startNight}")
    private int startNight;

    @Value("${properties.endNight}")
    private int endNight;

    public BigDecimal getMaxAmount() {
        return maxAmount;
    }

    public BigDecimal getMinAmount() {
        return minAmount;
    }

    public Long getMaxTermMonth() {
        return maxTermMonth;
    }

    public Long getMinTermMonth() {
        return minTermMonth;
    }

    public Long getExtendMonth() {
        return extendMonth;
    }

    public Long getInterest() {
        return interest;
    }

    public LocalTime getStartNight(){
        return LocalTime.of(startNight,00,00);
    }

    public LocalTime getEndNight(){
        return LocalTime.of(endNight,00,00);
    }
}
