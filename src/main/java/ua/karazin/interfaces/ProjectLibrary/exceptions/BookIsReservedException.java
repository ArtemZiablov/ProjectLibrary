package ua.karazin.interfaces.ProjectLibrary.exceptions;

public class BookIsReservedException extends BusinessException{
    public BookIsReservedException() {
        super("Book is reserved");
    }
}
