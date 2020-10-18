package pl.sda.microcredit;

import config.LoanProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(LoanProperties.class)
public class MicrocreditApplication {

    public static void main(String[] args) {
        SpringApplication.run(MicrocreditApplication.class, args);
    }

}
