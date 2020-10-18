package pl.sda.microcredit.exception;

public class LoanIsOutOfTerm extends CreditException {
    public LoanIsOutOfTerm(String message) {
        super(message);
    }
}
