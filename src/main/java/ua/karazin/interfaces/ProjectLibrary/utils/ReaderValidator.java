package ua.karazin.interfaces.ProjectLibrary.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ua.karazin.interfaces.ProjectLibrary.models.Reader;
import ua.karazin.interfaces.ProjectLibrary.services.ReaderService;

@RequiredArgsConstructor
@Component
public class ReaderValidator implements Validator {
    private final ReaderService readerService;


    @Override
    public boolean supports(Class<?> clazz) {
        return Reader.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Reader reader = (Reader) target;

        if (readerService.findByEmail(reader.getEmail()).isPresent())
            errors.rejectValue("email", "reader.fullNameExists");
        else if (readerService.findByFullName(reader.getFullName()).isPresent() &&
                readerService.findByDateOfBirth(reader.getDateOfBirth()).isPresent()) {
            errors.rejectValue("fullName", "Reader with same name and date-of-birth already exists");
            errors.rejectValue("dateOfBirth", "Reader with same date-of-birth and name already exists");
        }

    }
}
