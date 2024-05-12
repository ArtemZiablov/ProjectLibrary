package ua.karazin.interfaces.ProjectLibrary.exceptions;

public class NotAuthenticatedException extends BusinessException {
    public NotAuthenticatedException() {
        super("Not authenticated");
    }
}
