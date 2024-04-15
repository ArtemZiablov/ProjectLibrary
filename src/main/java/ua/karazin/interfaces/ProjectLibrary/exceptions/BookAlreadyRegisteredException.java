package ua.karazin.interfaces.ProjectLibrary.exceptions;

public class BookAlreadyRegisteredException extends BusinessException{
    public BookAlreadyRegisteredException(){
        super("The book has already been registered in the system!");
    }
}
