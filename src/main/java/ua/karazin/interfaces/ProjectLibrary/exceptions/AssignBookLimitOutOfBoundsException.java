package ua.karazin.interfaces.ProjectLibrary.exceptions;

public class AssignBookLimitOutOfBoundsException extends BusinessException {
    public AssignBookLimitOutOfBoundsException(){
        super("Reader already has maximum amount of books!");
    }
}
