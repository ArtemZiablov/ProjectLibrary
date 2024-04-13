package ua.karazin.interfaces.ProjectLibrary.exceptions;

public class BookCopyNotFoundException extends BusinessException {
    public BookCopyNotFoundException() {
        super("There is no such book copy in the system!");
    }
}
