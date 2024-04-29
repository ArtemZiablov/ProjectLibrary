package ua.karazin.interfaces.ProjectLibrary.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.karazin.interfaces.ProjectLibrary.dto.RegisterLibrarianDTO;
import ua.karazin.interfaces.ProjectLibrary.dto.RegisterReaderDTO;
import ua.karazin.interfaces.ProjectLibrary.models.Librarian;
import ua.karazin.interfaces.ProjectLibrary.models.Reader;
import ua.karazin.interfaces.ProjectLibrary.services.RegistrationService;


@RestController
@Slf4j(topic = "AuthController")
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final RegistrationService registrationService;


    @PostMapping("/registration/reader")
    public ResponseEntity<HttpStatus> registerReader(@RequestBody RegisterReaderDTO reader) {
        log.info("Registering reader: " + reader.fullName());
        var readerToRegister = new Reader(
                reader.fullName(),
                reader.dateOfBirth(),
                reader.password(),
                reader.phoneNumber(),
                reader.email(),
                reader.photo()
        );
        registrationService.registerReader(readerToRegister);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/registration/librarian")
    public ResponseEntity<HttpStatus> registerLibrarian(@RequestBody RegisterLibrarianDTO librarian) {
        log.info("Registering librarian: " + librarian.fullName());
        var librarianToRegister = new Librarian(
                librarian.fullName(),
                librarian.password(),
                librarian.phoneNumber(),
                librarian.email(),
                librarian.photo()
        );
        registrationService.registerLibrarian(librarianToRegister);

        return ResponseEntity.ok(HttpStatus.OK);
    }
}
