package ua.karazin.interfaces.ProjectLibrary.exceptions;

public class AdminNotExistException extends BusinessException{
    public AdminNotExistException() {
        super("Admin does not exist");
    }
}
