package ua.karazin.interfaces.ProjectLibrary.exceptions;

public class NoSearchedParametersWereProvidedException extends BusinessException{
    public NoSearchedParametersWereProvidedException() {
        super("No searched parameters were provided!");
    }
}
