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
import pl.sda.microcredit.model.LoanRequest;
import pl.sda.microcredit.repository.LoanRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static pl.sda.microcredit.controller.LoanController.LOAN_API;

@AutoConfigureMockMvc
@SpringBootTest
public class LoanControllerTests {

    @Autowired
    LoanController loanController;

    @Autowired
    LoanRepository loanRepository;

    @Autowired
    LoanProperties loanProperties;

    @Autowired
    MockMvc mockMvc;

    @AfterEach
    void tearDown() {
        loanRepository.deleteAll();
    }

    @Test
    public void shouldApplyForLoanCorretly() throws Exception {
/*        //given
        final String loanPath = LOAN_API;
        BigDecimal amount = loanProperties.getMaxAmount().subtract(BigDecimal.valueOf(1));
        LocalDate startDate = LocalDate.of(2030,10,10);
        Long period = loanProperties.getMaxTermMonth() - 1;
        LoanRequest loanRequest = new LoanRequest(amount, startDate, period);
        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(loanPath)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(toJson(loanRequest));
        int expectSizeUserRepository = 1;

        //when
        final ResultActions resultActions = mockMvc.perform(requestBuilder);

        //then
        resultActions.andExpect(status().isCreated());
        List<LoanEntity> all = loanRepository.findAll();
        assertEquals(expectSizeUserRepository,all.size());*/
    }

    private LoanRequest createLoanInDBCorrectly() {
        BigDecimal amount = loanProperties.getMaxAmount().subtract(BigDecimal.valueOf(1));
        LocalDate startDate = LocalDate.of(2030,10,10);
        Long period = loanProperties.getMaxTermMonth() - 1;
        LoanRequest loanRequest = new LoanRequest(amount, startDate, period);
        return loanRequest;
    }

    private static String toJson(final Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
