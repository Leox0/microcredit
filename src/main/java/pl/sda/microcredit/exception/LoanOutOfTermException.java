package pl.sda.microcredit.exception;

public class LoanOutOfTermException extends GenericLoanException {
    public LoanOutOfTermException(String message) {
        super(message);
    }
}
