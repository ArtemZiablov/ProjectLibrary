package ua.karazin.interfaces.ProjectLibrary.exceptions;

public class ReaderIsDebtorException extends BusinessException {
    public ReaderIsDebtorException() {
        super("The reader has status debtor");
    }
}
