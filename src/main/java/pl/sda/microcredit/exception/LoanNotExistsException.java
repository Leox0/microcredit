package pl.sda.microcredit.exception;

public class LoanNotExistsException extends GenericLoanException {
    public LoanNotExistsException(String message) {
        super(message);
    }
}
