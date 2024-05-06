package ua.karazin.interfaces.ProjectLibrary.exceptions;

public class BookReservationAlreadyExistException extends BusinessException{
    public BookReservationAlreadyExistException() {
        super("Book reservation already exists");
    }
}
