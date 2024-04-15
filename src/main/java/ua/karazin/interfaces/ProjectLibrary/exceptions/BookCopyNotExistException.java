package ua.karazin.interfaces.ProjectLibrary.exceptions;

public class BookCopyNotExistException extends BusinessException {
    public BookCopyNotExistException() {
        super("There is no such book copy in the system!");
    }
}
