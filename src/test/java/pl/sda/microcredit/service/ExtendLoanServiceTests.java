package pl.sda.microcredit.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.sda.microcredit.config.LoanProperties;
import pl.sda.microcredit.exception.GenericLoanException;
import pl.sda.microcredit.exception.LoanAlreadyExtendedException;
import pl.sda.microcredit.exception.LoanNotExistsException;
import pl.sda.microcredit.model.LoanRequest;
import pl.sda.microcredit.model.LoanView;
import pl.sda.microcredit.repository.LoanRepository;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ExtendLoanServiceTests {

    @Autowired
    private LoanService loanService;

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private LoanProperties loanProperties;

    @AfterEach
    void tearDown() {
        loanRepository.deleteAll();
    }

    @Test
    public void shouldExtendTermLoanCorrectly() throws GenericLoanException {
        //given
        loanService.applyForLoan(createLoanInDBCorrectly());
        LoanView loan = loanRepository.findAll()
                .stream()
                .findFirst()
                .get()
                .toView();
        LocalDate endDateBeforExtend = loan.getEndDate();
        Long extendMonth = loanProperties.getExtendMonth();

        //when
        loanService.extendTermLoan(loan.getId());

        //then
        loan = loanRepository.findAll()
                .stream()
                .findFirst()
                .get()
                .toView();
        assertEquals(extendMonth, loan.getExtendTerm());
        assertEquals(endDateBeforExtend.plusMonths(extendMonth), loan.getEndDate());
    }

    @Test
    public void shouldThrowExceptionWhenExtendLoanSecoundTime() throws GenericLoanException {
        //given
        loanService.applyForLoan(createLoanInDBCorrectly());
        LoanView loan = loanRepository.findAll()
                .stream()
                .findFirst()
                .get()
                .toView();
        loanService.extendTermLoan(loan.getId());
        LocalDate endDateBeforExtend = loan.getEndDate();
        Long extendMonth = loanProperties.getExtendMonth();

        //when
        LoanView finalLoan = loan;
        Executable executable = () -> loanService.extendTermLoan(finalLoan.getId());

        //then
        loan = loanRepository.findAll()
                .stream()
                .findFirst()
                .get()
                .toView();
        assertEquals(extendMonth, loan.getExtendTerm());
        assertEquals(endDateBeforExtend.plusMonths(extendMonth), loan.getEndDate());
        assertThrows(LoanAlreadyExtendedException.class, executable);
    }

    @Test
    public void shouldThrowExceptionWhenExtendLoanDoesntExist() {
        //given

        //when
        Executable executable = () -> loanService.extendTermLoan(1L);

        //then
        assertThrows(LoanNotExistsException.class, executable);
    }

    private LoanRequest createLoanInDBCorrectly() {
        BigDecimal amount = loanProperties.getMaxAmount().subtract(BigDecimal.valueOf(1));
        LocalDate startDate = LocalDate.now();
        Long period = loanProperties.getMaxTermMonth() - 1;
        LoanRequest loanRequest = new LoanRequest(amount, startDate, period);
        return loanRequest;
    }

}
