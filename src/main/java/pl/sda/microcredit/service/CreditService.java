package pl.sda.microcredit.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.sda.microcredit.exception.LoanIsOutOfLimitException;
import pl.sda.microcredit.exception.LoanIsOutOfTerm;
import pl.sda.microcredit.model.LoanEntity;
import pl.sda.microcredit.model.LoanRequest;
import pl.sda.microcredit.model.LoanView;
import pl.sda.microcredit.repository.CreditRepository;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class CreditService {

    public static final String LOAN_IS_OUT_OF_LIMIT = "Loan is out of limit";
    public static final String LOAN_IS_OUT_OF_TERM = "Loan is out of term";
    private final CreditRepository creditRepository;
    private final BigDecimal maxAmount;
    private final BigDecimal minAmount;
    private final Long maxTermMonth;
    private final Long minTermMonth;
    private final Long interest;


    public CreditService(CreditRepository creditRepository,
                         @Value("${properties.maxAmount}") BigDecimal maxAmount,
                         @Value("${properties.minAmount}") BigDecimal minAmount,
                         @Value("${properties.maxTermMonth}") Long maxTermMonth,
                         @Value("${properties.minTermMonth}") Long minTermMonth,
                         @Value("${properties.interest}") Long interest) {
        this.creditRepository = creditRepository;
        this.maxAmount = maxAmount;
        this.minAmount = minAmount;
        this.maxTermMonth = maxTermMonth;
        this.minTermMonth = minTermMonth;
        this.interest = interest;
    }

    public void applyForLoan(LoanRequest loan) {
        assertLoanIsInTerm(loan);
        assertLoanIsInLimitAmount(loan);
        LoanEntity loanEntity = LoanEntity.builder()
                .amount(loan.getAmount())
                .amountInterest(loan.getAmount()
                        .multiply(BigDecimal.valueOf(interest))
                        .divide(BigDecimal.valueOf(100)))
                .startDate(loan.getStartDate())
                .endDate(loan.getStartDate().plusMonths(loan.getPeriod()))
                .period(loan.getPeriod())
                .createDate(LocalDate.now())
                .build();
        creditRepository.save(loanEntity);
    }

    private void assertLoanIsInLimitAmount(LoanRequest loan) {
        BigDecimal amount = loan.getAmount();
        if (amount.compareTo(minAmount) == -1 || amount.compareTo(maxAmount) == 1) {
            throw new LoanIsOutOfLimitException(LOAN_IS_OUT_OF_LIMIT);
        }
    }

    private void assertLoanIsInTerm(LoanRequest loan) {
        Long period = loan.getPeriod();
        if (period.compareTo(minTermMonth) == -1 || period.compareTo(maxTermMonth) == 1) {
            throw new LoanIsOutOfTerm(LOAN_IS_OUT_OF_TERM);
        }
    }

}
