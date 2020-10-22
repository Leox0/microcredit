package pl.sda.microcredit.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pl.sda.microcredit.config.LoanProperties;
import pl.sda.microcredit.exception.LoanAlreadyExtendedException;
import pl.sda.microcredit.model.LoanEntity;
import pl.sda.microcredit.model.LoanRequest;
import pl.sda.microcredit.repository.LoanRepository;
import pl.sda.microcredit.service.LoanService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static pl.sda.microcredit.controller.LoanController.LOAN_API;
import static pl.sda.microcredit.service.LoanService.*;

@AutoConfigureMockMvc
@SpringBootTest
public class ExtendLoanControllerTests {

    final String loanExtendPath = LOAN_API + "/extend/";

    @Autowired
    LoanController loanController;

    @Autowired
    LoanRepository loanRepository;

    @Autowired
    LoanProperties loanProperties;

    @Autowired
    LoanService loanService;

    @Autowired
    MockMvc mockMvc;

    @AfterEach
    void tearDown() {
        loanRepository.deleteAll();
    }

    @Test
    public void shouldExtendLoanCorrectly() throws Exception {
        //given
        loanService.applyForLoan(createLoanInDBCorrectly());
        Long loanId = loanRepository.findAll()
                .stream().findFirst().get().toView().getId();
        String loanPath = loanExtendPath + loanId;
        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(loanPath)
                .contentType(MediaType.APPLICATION_JSON_VALUE);

        //when
        final ResultActions resultActions = mockMvc.perform(requestBuilder);

        //then
        resultActions.andExpect(status().isOk());
        LoanEntity loan = loanRepository.findAll().stream().findFirst().get();
        assertEquals(loanProperties.getExtendMonth(), loan.toView().getExtendTerm());
    }

    @Test
    public void shouldGetStatusNotFoundWhenLoanAlreadyExtended() throws Exception {
        //given
        loanService.applyForLoan(createLoanInDBCorrectly());
        Long loanId = loanRepository.findAll()
                .stream().findFirst().get().toView().getId();
        String loanPath = loanExtendPath + loanId;
        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(loanPath)
                .contentType(MediaType.APPLICATION_JSON_VALUE);
        final ResultActions resultActions = mockMvc.perform(requestBuilder);

        //when
        final ResultActions resultActions2 = mockMvc.perform(requestBuilder);

        //then
        resultActions2.andExpect(status().isNotFound());
        LoanEntity loan = loanRepository.findAll().stream().findFirst().get();
        assertEquals(loanProperties.getExtendMonth(), loan.toView().getExtendTerm());
        String resultResponse = resultActions2.andReturn().getResponse().getContentAsString();
        assertEquals(LOAN_ALREADY_EXTENDED, resultResponse);
    }

    @Test
    public void shouldGetStatusNotFoundWhenWhenExtendLoanDoesntExist() throws Exception {
        //given
        String loanPath = loanExtendPath + 10;
        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(loanPath)
                .contentType(MediaType.APPLICATION_JSON_VALUE);

        //when
        final ResultActions resultActions = mockMvc.perform(requestBuilder);

        //then
        resultActions.andExpect(status().isNotFound());
        String resultResponse = resultActions.andReturn().getResponse().getContentAsString();
        assertEquals(LOAN_NOT_EXISTS, resultResponse);
    }

    private LoanRequest createLoanInDBCorrectly() {
        BigDecimal amount = loanProperties.getMaxAmount().subtract(BigDecimal.valueOf(1));
        LocalDate startDate = LocalDate.now();
        Long period = loanProperties.getMaxTermMonth() - 1;
        LoanRequest loanRequest = new LoanRequest(amount, startDate, period);
        return loanRequest;
    }
}
