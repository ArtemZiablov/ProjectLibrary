package ua.karazin.interfaces.ProjectLibrary.exceptions;

public class BookIsAlreadyTakenByReaderException extends BusinessException{
    public BookIsAlreadyTakenByReaderException() {
        super("Book is already taken by reader");
    }
}
