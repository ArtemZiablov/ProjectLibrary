package ua.karazin.interfaces.ProjectLibrary.exceptions;

public class BookOperationDoesntExistException extends BusinessException{
    public BookOperationDoesntExistException(){
        super("There is no book operation with this book and reader");
    }
}
