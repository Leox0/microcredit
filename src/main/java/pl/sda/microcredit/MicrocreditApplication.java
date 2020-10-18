package pl.sda.microcredit;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import pl.sda.microcredit.config.LoanProperties;

@SpringBootApplication
@EnableConfigurationProperties(LoanProperties.class)
public class MicrocreditApplication {

    public static void main(String[] args) {
        SpringApplication.run(MicrocreditApplication.class, args);
    }

}
