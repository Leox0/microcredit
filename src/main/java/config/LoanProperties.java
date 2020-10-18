package config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Configuration
@ConfigurationProperties(prefix = "properties")
@Component
@Getter
public class LoanProperties {

    @Value("${maxAmount}")
    private BigDecimal maxAmount;

    @Value("${minAmount}")
    private BigDecimal minAmount;

    @Value("${maxTermMonth}")
    private Long maxTermMonth;

    @Value("${minTermMonth}")
    private Long minTermMonth;

    @Value("${extendMonth}")
    private Long extendMonth;

    @Value("${interest}")
    private Long interest;

}
