package pl.sda.microcredit.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.sda.microcredit.model.LoanRequest;
import pl.sda.microcredit.service.LoanService;

@RestController
@RequestMapping("/credit")
public class LoanController {

    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @PostMapping
    public ResponseEntity<Void> applyForLoan(@RequestBody LoanRequest loanRequest) {
        loanService.applyForLoan(loanRequest);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Void> applyForLoan(@PathVariable(value = "id") Long loanId) {
        loanService.extendTermLoan(loanId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

}
