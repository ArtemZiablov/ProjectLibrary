package ua.karazin.interfaces.ProjectLibrary.exceptions;

public class ReadersNotFoundException extends BusinessException{
    public ReadersNotFoundException(){
        super("No readers were found");
    }
}
