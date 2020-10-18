package pl.sda.microcredit.exception;

public class LoanOutOfLimitException extends GenericLoanException {
    public LoanOutOfLimitException(String message) {
        super(message);
    }
}
