package pl.sda.microcredit.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import pl.sda.microcredit.model.LoanEntity;
import pl.sda.microcredit.repository.LoanRepository;
import pl.sda.microcredit.service.LoanService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static pl.sda.microcredit.controller.LoanController.LOAN_API;
import static pl.sda.microcredit.service.LoanService.*;

@AutoConfigureMockMvc
@SpringBootTest
public class RequestLoanControllerTests {

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
    public void shouldApplyForLoanCorretly() throws Exception {
        //given
        BigDecimal amount = loanProperties.getMaxAmount().subtract(BigDecimal.valueOf(1));
        String startDate = "2021-10-20";
        Long period = loanProperties.getMaxTermMonth() - 1;
        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(LOAN_API)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("{\n" +
                        "    \"amount\": \"" + amount + "\",\n" +
                        "    \"startDate\" : \"" + startDate + "\",\n" +
                        "    \"period\": " + period + "\n" +
                        "}");
        int expectSizeUserRepository = 1;

        //when
        final ResultActions resultActions = mockMvc.perform(requestBuilder);

        //then
        resultActions.andExpect(status().isCreated());
        List<LoanEntity> all = loanRepository.findAll();
        LoanEntity loan = all.stream().findFirst().get();
        assertEquals(expectSizeUserRepository, all.size());
        assertEquals(LocalDate.parse(startDate), loan.toView().getStartDate());
    }

    @Test
    public void shouldGetStatusNotFoundWhenLoanRequestAmountIsOutOfLimit() throws Exception {
        //given
        BigDecimal amount = loanProperties.getMaxAmount().add(BigDecimal.valueOf(1));
        String startDate = "2021-10-20";
        Long period = loanProperties.getMaxTermMonth() - 1;
        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(LOAN_API)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("{\n" +
                        "    \"amount\": \"" + amount + "\",\n" +
                        "    \"startDate\" : \"" + startDate + "\",\n" +
                        "    \"period\": " + period + "\n" +
                        "}");

        //when
        final ResultActions resultActions = mockMvc.perform(requestBuilder);

        //then
        resultActions.andExpect(status().isNotFound());
        assertTrue(loanRepository.findAll().isEmpty());
        String resultResponse = resultActions.andReturn().getResponse().getContentAsString();
        assertEquals(LOAN_OUT_OF_LIMIT, resultResponse);
    }

    @Test
    public void shouldGetStatusNotFoundWhenLoanRequestAmountIsOutOfTerm() throws Exception {
        //given
        BigDecimal amount = loanProperties.getMaxAmount().subtract(BigDecimal.valueOf(1));
        String startDate = "2021-10-20";
        Long period = loanProperties.getMaxTermMonth() + 1;
        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(LOAN_API)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("{\n" +
                        "    \"amount\": \"" + amount + "\",\n" +
                        "    \"startDate\" : \"" + startDate + "\",\n" +
                        "    \"period\": " + period + "\n" +
                        "}");

        //when
        final ResultActions resultActions = mockMvc.perform(requestBuilder);

        //then
        resultActions.andExpect(status().isNotFound());
        assertTrue(loanRepository.findAll().isEmpty());
        String resultResponse = resultActions.andReturn().getResponse().getContentAsString();
        assertEquals(LOAN_OUT_OF_TERM, resultResponse);
    }

}
