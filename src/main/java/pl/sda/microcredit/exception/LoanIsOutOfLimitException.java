package pl.sda.microcredit.exception;

public class LoanIsOutOfLimitException extends CreditException {
    public LoanIsOutOfLimitException(String message) {
        super(message);
    }
}
