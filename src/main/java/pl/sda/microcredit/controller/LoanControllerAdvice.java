package pl.sda.microcredit.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pl.sda.microcredit.exception.GenericLoanException;

@ControllerAdvice
public class LoanControllerAdvice {

    @ExceptionHandler(GenericLoanException.class)
    public ResponseEntity<String> handException(GenericLoanException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
    }

}
