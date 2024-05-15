package ua.karazin.interfaces.ProjectLibrary.exceptions;

public class WrongIsbnException extends BusinessException {
    public WrongIsbnException() {
        super("Wrong ISBN was provided");
    }
}
