package ua.karazin.interfaces.ProjectLibrary.controllers;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ua.karazin.interfaces.ProjectLibrary.dto.ErrorDTO;
import ua.karazin.interfaces.ProjectLibrary.exceptions.*;


import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_ACCEPTABLE;

@RestControllerAdvice
public class ErrorControllerAdvice {

    @ResponseStatus(NOT_ACCEPTABLE)
    @ExceptionHandler({
            BookAlreadyRegisteredException.class,
            ReaderIsDebtorException.class,
            AssignBookLimitOutOfBoundsException.class,
            BookNotReturnedFromReaderException.class,
            NoSearchedParametersWereProvidedException.class
    })
    ErrorDTO notAcceptableExceptionHandler(BusinessException ex){
        return new ErrorDTO(ex.getMessage());
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler({
            BookCopyNotExistException.class,
            BookNotRegisteredException.class,
            BookCopyNotExistException.class,
            ReaderNotExistException.class,
            BookOperationDoesntExistException.class
    })
    ErrorDTO badReqExceptionHandler(BusinessException ex){
        return new ErrorDTO(ex.getMessage());
    }
}
