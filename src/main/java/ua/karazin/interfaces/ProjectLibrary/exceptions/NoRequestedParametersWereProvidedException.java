package ua.karazin.interfaces.ProjectLibrary.exceptions;

public class NoRequestedParametersWereProvidedException extends BusinessException{
    public NoRequestedParametersWereProvidedException() {
        super("No requested parameters were provided!");
    }
}
