package pl.sda.microcredit.service;

import org.springframework.stereotype.Service;
import pl.sda.microcredit.exception.*;
import pl.sda.microcredit.model.LoanEntity;
import pl.sda.microcredit.model.LoanRequest;
import pl.sda.microcredit.config.LoanProperties;
import pl.sda.microcredit.repository.LoanRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Service
public class LoanService {

    public static final String LOAN_OUT_OF_LIMIT = "Loan is out of limit";
    public static final String LOAN_OUT_OF_TERM = "Loan is out of term";
    public static final String LOAN_NOT_EXISTS = "Loan for given id doesnt exists";
    public static final String LOAN_ALREADY_EXTENDED = "Loan already extended";
    public static final String MAX_AMOUNT_IN_NIGHT_IS_NOT_ALLOWED = "Loan with max amount in night is not allowed";
    private final LoanRepository loanRepository;
    private final LoanProperties loanProperties;

    public LoanService(LoanRepository loanRepository, LoanProperties loanProperties) {
        this.loanRepository = loanRepository;
        this.loanProperties = loanProperties;
    }

    public void applyForLoan(LoanRequest loan) throws GenericLoanException {
        assertLoanIsInTerm(loan);
        assertLoanIsInLimitAmount(loan);
        assertLoanInNightWithNoMaxAmount(loan);
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

    public void extendTermLoan(Long id) throws LoanNotExistsException, LoanAlreadyExtendedException {
        LoanEntity loan = loanRepository.findById(id).orElseThrow(() -> new LoanNotExistsException(LOAN_NOT_EXISTS));
        Long extendMonth = loanProperties.getExtendMonth();
        assertLoanNoExtendYet(loan.toView().getExtendTerm());
        loan.extend(extendMonth);
        loanRepository.save(loan);
    }

    private void assertLoanNoExtendYet(Long extendMonth) throws LoanAlreadyExtendedException {
        if (extendMonth != null) {
            throw new LoanAlreadyExtendedException(LOAN_ALREADY_EXTENDED);
        }
    }

    private void assertLoanIsInLimitAmount(LoanRequest loan) throws LoanOutOfLimitException {
        BigDecimal amount = loan.getAmount();
        if (amount.compareTo(loanProperties.getMinAmount()) == -1 || amount.compareTo(loanProperties.getMaxAmount()) == 1) {
            throw new LoanOutOfLimitException(LOAN_OUT_OF_LIMIT);
        }
    }

    private void assertLoanIsInTerm(LoanRequest loan) throws LoanOutOfTermException {
        Long period = loan.getPeriod();
        if (period.compareTo(loanProperties.getMinTermMonth()) == -1 || period.compareTo(loanProperties.getMaxTermMonth()) == 1) {
            throw new LoanOutOfTermException(LOAN_OUT_OF_TERM);
        }
    }

    private void assertLoanInNightWithNoMaxAmount(LoanRequest loan) throws LoanMaxAmountInNightException {
        if (LocalTime.now().isAfter(loanProperties.getStartNight())
                && LocalTime.now().isBefore(loanProperties.getEndNight())
                && loan.getAmount().compareTo(loanProperties.getMaxAmount()) == 0) {
            throw new LoanMaxAmountInNightException(MAX_AMOUNT_IN_NIGHT_IS_NOT_ALLOWED);
        }
    }

}
