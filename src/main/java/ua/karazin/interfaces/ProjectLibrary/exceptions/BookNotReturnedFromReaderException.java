package ua.karazin.interfaces.ProjectLibrary.exceptions;

public class BookNotReturnedFromReaderException extends BusinessException {
    public BookNotReturnedFromReaderException() {
        super("The book has not been returned from reader!");
    }
}
