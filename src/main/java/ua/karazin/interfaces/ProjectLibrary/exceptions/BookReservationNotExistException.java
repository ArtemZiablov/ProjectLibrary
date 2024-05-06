package ua.karazin.interfaces.ProjectLibrary.exceptions;

public class BookReservationNotExistException extends BusinessException{
    public BookReservationNotExistException() {
        super("Book Reservation Not Exist");
    }
}
