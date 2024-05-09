package ua.karazin.interfaces.ProjectLibrary.exceptions;

public class OpenBookOperationAlreadyExistException extends BusinessException{
    public OpenBookOperationAlreadyExistException() {
        super("Open book-operation already exists!");
    }
}
