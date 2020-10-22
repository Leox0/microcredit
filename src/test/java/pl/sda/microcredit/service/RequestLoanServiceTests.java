package pl.sda.microcredit.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.sda.microcredit.config.LoanProperties;
import pl.sda.microcredit.exception.GenericLoanException;
import pl.sda.microcredit.exception.LoanMaxAmountInNightException;
import pl.sda.microcredit.exception.LoanOutOfLimitException;
import pl.sda.microcredit.exception.LoanOutOfTermException;
import pl.sda.microcredit.model.LoanEntity;
import pl.sda.microcredit.model.LoanRequest;
import pl.sda.microcredit.model.LoanView;
import pl.sda.microcredit.repository.LoanRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class RequestLoanServiceTests {

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
    public void shouldApplyLoanCorrectly() throws GenericLoanException {
        //given
        BigDecimal amount = loanProperties.getMaxAmount().subtract(BigDecimal.valueOf(1));
        LocalDate startDate = LocalDate.now();
        Long period = loanProperties.getMaxTermMonth() - 1;
        LoanRequest loanRequest = new LoanRequest(amount, startDate, period);
        int expectSizeUserRepository = 1;

        //when
        loanService.applyForLoan(loanRequest);

        //then
        LoanView loan = loanRepository.findAll()
                .stream()
                .findFirst()
                .get()
                .toView();
        List<LoanEntity> all = loanRepository.findAll();
        assertEquals(expectSizeUserRepository,all.size());
        assertTrue(amount.compareTo(loan.getAmount()) == 0);
        assertEquals(startDate, loan.getStartDate());
        assertEquals(period, loan.getPeriod());
    }

    @Test
    public void shouldThrowExceptionWhenRequestLoanAmountIsToHigh() {
        //given
        BigDecimal amount = loanProperties.getMaxAmount().add(BigDecimal.valueOf(1));
        LocalDate startDate = LocalDate.now();
        Long period = loanProperties.getMaxTermMonth() - 1;
        LoanRequest loanRequest = new LoanRequest(amount, startDate, period);

        //when
        Executable executable = () -> loanService.applyForLoan(loanRequest);

        //then
        assertTrue(loanRepository.findAll().isEmpty());
        assertThrows(LoanOutOfLimitException.class, executable);
    }

    @Test
    public void shouldThrowExceptionWhenRequestLoanAmountIsToLow() {
        //given
        BigDecimal amount = loanProperties.getMinAmount().subtract(BigDecimal.valueOf(1));
        LocalDate startDate = LocalDate.now();
        Long period = loanProperties.getMaxTermMonth() - 1;
        LoanRequest loanRequest = new LoanRequest(amount, startDate, period);

        //when
        Executable executable = () -> loanService.applyForLoan(loanRequest);

        //then
        assertTrue(loanRepository.findAll().isEmpty());
        assertThrows(LoanOutOfLimitException.class, executable);
    }

    @Test
    public void shouldThrowExceptionWhenRequestLoanTermIsToLong() {
        //given
        BigDecimal amount = loanProperties.getMaxAmount().subtract(BigDecimal.valueOf(1));
        LocalDate startDate = LocalDate.now();
        Long period = loanProperties.getMaxTermMonth() + 1;
        LoanRequest loanRequest = new LoanRequest(amount, startDate, period);

        //when
        Executable executable = () -> loanService.applyForLoan(loanRequest);

        //then
        assertTrue(loanRepository.findAll().isEmpty());
        assertThrows(LoanOutOfTermException.class, executable);
    }

    @Test
    public void shouldThrowExceptionWhenRequestLoanTermIsToShort() {
        //given
        BigDecimal amount = loanProperties.getMaxAmount().subtract(BigDecimal.valueOf(1));
        LocalDate startDate = LocalDate.now();
        Long period = loanProperties.getMinTermMonth() - 1;
        LoanRequest loanRequest = new LoanRequest(amount, startDate, period);

        //when
        Executable executable = () -> loanService.applyForLoan(loanRequest);

        //then
        assertTrue(loanRepository.findAll().isEmpty());
        assertThrows(LoanOutOfTermException.class, executable);
    }

    //pass only in night
    @Test
    public void shouldThrowExceptionWhenRequestLoanInNightWithMaxAmount(){
        //given
        BigDecimal amount = loanProperties.getMaxAmount();
        LocalDate startDate = LocalDate.now();
        Long period = loanProperties.getMaxTermMonth() - 1;
        LoanRequest loanRequest = new LoanRequest(amount, startDate, period);

        //when
        Executable executable = () -> loanService.applyForLoan(loanRequest);

        //then
        assertTrue(loanRepository.findAll().isEmpty());
        assertThrows(LoanMaxAmountInNightException.class, executable);
    }

}
