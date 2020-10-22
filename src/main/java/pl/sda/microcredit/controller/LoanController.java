package pl.sda.microcredit.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.sda.microcredit.exception.GenericLoanException;
import pl.sda.microcredit.exception.LoanAlreadyExtendedException;
import pl.sda.microcredit.exception.LoanNotExistsException;
import pl.sda.microcredit.model.LoanRequest;
import pl.sda.microcredit.service.LoanService;

@RestController
@RequestMapping(LoanController.LOAN_API)
public class LoanController {

    public static final String LOAN_API = "/credit";

    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @PostMapping
    public ResponseEntity<Void> applyForLoan(@RequestBody LoanRequest loanRequest) throws GenericLoanException {
        loanService.applyForLoan(loanRequest);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    @GetMapping("/extend/{id}")
    public ResponseEntity<Void> applyForLoan(@PathVariable(value = "id") Long loanId) throws LoanNotExistsException, LoanAlreadyExtendedException {
        loanService.extendTermLoan(loanId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

}
