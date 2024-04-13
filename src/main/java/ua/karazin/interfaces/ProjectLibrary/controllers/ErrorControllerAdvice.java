package ua.karazin.interfaces.ProjectLibrary.controllers;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ua.karazin.interfaces.ProjectLibrary.dto.ErrorDTO;
import ua.karazin.interfaces.ProjectLibrary.exceptions.BusinessException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestControllerAdvice
public class ErrorControllerAdvice {

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler()
    ErrorDTO exceptionHandler(BusinessException ex){
        return new ErrorDTO(ex.getMessage());
    }
}
