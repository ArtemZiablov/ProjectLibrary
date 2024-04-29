package ua.karazin.interfaces.ProjectLibrary.exceptions;

public class LibrarianNotExistException extends BusinessException{
    public LibrarianNotExistException() {
        super("Such librarian doesnt exist!");
    }
}
