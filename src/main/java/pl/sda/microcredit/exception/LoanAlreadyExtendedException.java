package pl.sda.microcredit.exception;

public class LoanAlreadyExtendedException extends GenericLoanException{
    public LoanAlreadyExtendedException(String message) {
        super(message);
    }
}
