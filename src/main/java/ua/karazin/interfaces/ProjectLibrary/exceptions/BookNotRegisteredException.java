package ua.karazin.interfaces.ProjectLibrary.exceptions;

public class BookNotRegisteredException extends BusinessException {
    public BookNotRegisteredException() {
        super("The book has been not registered in the system!");
    }
}
