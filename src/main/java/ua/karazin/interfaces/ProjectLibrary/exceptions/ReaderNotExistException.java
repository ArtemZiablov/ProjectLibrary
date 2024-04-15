package ua.karazin.interfaces.ProjectLibrary.exceptions;

public class ReaderNotExistException extends BusinessException {
    public ReaderNotExistException(){
        super("Such reader doesnt exist!");
    }
}
