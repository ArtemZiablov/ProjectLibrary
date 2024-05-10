package ua.karazin.interfaces.ProjectLibrary.exceptions;

public class ReservationBookLimitOutOfBoundsException extends BusinessException {
    public ReservationBookLimitOutOfBoundsException() {
        super("Reservation book limit is out of bounds");
    }
}
