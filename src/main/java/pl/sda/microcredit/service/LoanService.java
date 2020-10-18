package pl.sda.microcredit.service;

import org.springframework.stereotype.Service;
import pl.sda.microcredit.exception.LoanNotExistsException;
import pl.sda.microcredit.exception.LoanOutOfLimitException;
import pl.sda.microcredit.exception.LoanOutOfTermException;
import pl.sda.microcredit.model.LoanEntity;
import pl.sda.microcredit.model.LoanRequest;
import pl.sda.microcredit.config.LoanProperties;
import pl.sda.microcredit.repository.LoanRepository;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class LoanService {

    public static final String LOAN_OUT_OF_LIMIT = "Loan is out of limit";
    public static final String LOAN_OUT_OF_TERM = "Loan is out of term";
    public static final String LOAN_NOT_EXISTS = "Loan for given id doesnt exists";
    private final LoanRepository loanRepository;
    private final LoanProperties loanProperties;

    public LoanService(LoanRepository loanRepository, LoanProperties loanProperties) {
        this.loanRepository = loanRepository;
        this.loanProperties = loanProperties;
    }

    public void applyForLoan(LoanRequest loan) {
        assertLoanIsInTerm(loan);
        assertLoanIsInLimitAmount(loan);
        LoanEntity loanEntity = buildWith(loan);
        loanRepository.save(loanEntity);
    }

    private LoanEntity buildWith(LoanRequest loan) {
        return LoanEntity.builder()
                .amount(loan.getAmount())
                .amountInterest(amountInterestCalc(loan))
                .startDate(loan.getStartDate())
                .endDate(loan.getStartDate().plusMonths(loan.getPeriod()))
                .period(loan.getPeriod())
                .createDate(LocalDate.now())
                .build();
    }

    private BigDecimal amountInterestCalc(LoanRequest loan) {
        return loan.getAmount()
                .multiply(BigDecimal.valueOf(loanProperties.getInterest()))
                .divide(BigDecimal.valueOf(100));
    }

    public void extendTermLoan(Long id) {
        LoanEntity loan = loanRepository.findById(id).orElseThrow(() -> new LoanNotExistsException(LOAN_NOT_EXISTS));
        loan.extend(loanProperties.getExtendMonth());
    }

    private void assertLoanIsInLimitAmount(LoanRequest loan) {
        BigDecimal amount = loan.getAmount();
        if (amount.compareTo(loanProperties.getMinAmount()) == -1 || amount.compareTo(loanProperties.getMaxAmount()) == 1) {
            throw new LoanOutOfLimitException(LOAN_OUT_OF_LIMIT);
        }
    }

    private void assertLoanIsInTerm(LoanRequest loan) {
        Long period = loan.getPeriod();
        if (period.compareTo(loanProperties.getMinTermMonth()) == -1 || period.compareTo(loanProperties.getMaxTermMonth()) == 1) {
            throw new LoanOutOfTermException(LOAN_OUT_OF_TERM);
        }
    }

}
