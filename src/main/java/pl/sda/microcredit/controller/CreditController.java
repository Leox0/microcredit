package pl.sda.microcredit.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.sda.microcredit.model.LoanRequest;
import pl.sda.microcredit.service.CreditService;

@RestController
@RequestMapping("/credit")
public class CreditController {

    private final CreditService creditService;

    public CreditController(CreditService creditService) {
        this.creditService = creditService;
    }

    @PostMapping
    public ResponseEntity<Void> applyForLoan(@RequestBody LoanRequest loanRequest){
        creditService.applyForLoan(loanRequest);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

}
