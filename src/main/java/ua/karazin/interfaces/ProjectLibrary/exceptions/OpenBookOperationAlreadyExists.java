package ua.karazin.interfaces.ProjectLibrary.exceptions;

public class OpenBookOperationAlreadyExists extends BusinessException{
    public OpenBookOperationAlreadyExists() {
        super("Open book-operation already exists!");
    }
}
