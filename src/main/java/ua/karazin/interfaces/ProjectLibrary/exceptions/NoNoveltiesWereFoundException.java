package ua.karazin.interfaces.ProjectLibrary.exceptions;

public class NoNoveltiesWereFoundException extends BusinessException{
    public NoNoveltiesWereFoundException() {
        super("No novelties were found");
    }
}
