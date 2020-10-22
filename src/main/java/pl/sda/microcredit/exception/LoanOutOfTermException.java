package pl.sda.microcredit.exception;

import java.lang.reflect.Executable;

public class LoanOutOfTermException extends GenericLoanException {
    public LoanOutOfTermException(String message) {
        super(message);
    }
}
