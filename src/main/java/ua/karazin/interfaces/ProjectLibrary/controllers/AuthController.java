package ua.karazin.interfaces.ProjectLibrary.controllers;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ua.karazin.interfaces.ProjectLibrary.dto.AuthenticationDTO;
import ua.karazin.interfaces.ProjectLibrary.dto.RegisterLibrarianDTO;
import ua.karazin.interfaces.ProjectLibrary.dto.RegisterReaderDTO;
import ua.karazin.interfaces.ProjectLibrary.models.Admin;
import ua.karazin.interfaces.ProjectLibrary.models.Librarian;
import ua.karazin.interfaces.ProjectLibrary.models.Reader;
import ua.karazin.interfaces.ProjectLibrary.security.AdminDetails;
import ua.karazin.interfaces.ProjectLibrary.security.JWTUtil;
import ua.karazin.interfaces.ProjectLibrary.security.LibrarianDetails;
import ua.karazin.interfaces.ProjectLibrary.security.ReaderDetails;
import ua.karazin.interfaces.ProjectLibrary.services.RegistrationService;
import ua.karazin.interfaces.ProjectLibrary.utils.ReaderValidator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@RestController
@Slf4j(topic = "AuthController")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/auth")
public class AuthController {
    private final RegistrationService registrationService;
    private final ReaderValidator readerValidator;
    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;


    @PostMapping("/registration/admin")
    public ResponseEntity<HttpStatus> registerAdmin(@RequestBody HashMap<String, String> admin) {
        log.info("Registering admin: {}", admin.get("fullName"));
        var adminToRegister = new Admin(
                admin.get("fullName"),
                admin.get("phoneNumber"),
                admin.get("email"),
                admin.get("profilePhoto"),
                admin.get("password")
        );
        registrationService.registerAdmin(adminToRegister);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/registration/reader")
    public ResponseEntity<?> registerReader(@RequestBody RegisterReaderDTO reader, BindingResult bindingResult) {
        log.info("Registering reader: " + reader.fullName());

        var readerToRegister = new Reader(
                reader.fullName(),
                reader.dateOfBirth(),
                reader.password(),
                reader.phoneNumber(),
                reader.email(),
                reader.photo()
        );
        readerValidator.validate(readerToRegister, bindingResult);
        if (bindingResult.hasErrors()) {
            List<String> errorMessages = bindingResult.getFieldErrors().stream()
                    .map(FieldError::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessages);
        }
        registrationService.registerReader(readerToRegister);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/registration/multiple-readers")
    public ResponseEntity<HttpStatus> registerMultipleReaders(@RequestBody List<RegisterReaderDTO> readersToRegister) {
        log.info("Req to register multiple readers");

        List<Reader> readers = readersToRegister.stream()
                .map(reader -> new Reader(
                        reader.fullName(),
                        reader.dateOfBirth(),
                        reader.password(),
                        reader.phoneNumber(),
                        reader.email(),
                        reader.photo())
                ).toList();
        registrationService.registerMultipleReaders(readers);

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

    @PostMapping("/registration/multiple-librarians")
    public ResponseEntity<HttpStatus> registerMultipleLibrarian(@RequestBody List<RegisterLibrarianDTO> librariansToRegister) {
        log.info("Req to register multiple librarians");

        List<Librarian> librarians = librariansToRegister.stream()
                .map(librarian -> new Librarian(
                        librarian.fullName(),
                        librarian.password(),
                        librarian.phoneNumber(),
                        librarian.email(),
                        librarian.photo()
                )).toList();

        registrationService.registerMultipleLibrarians(librarians);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("login")
    public Map<String, String> performLogin(@RequestBody AuthenticationDTO authenticationDTO) {
        log.info("Performing login: {}", authenticationDTO.username());

        UsernamePasswordAuthenticationToken authInputToken =
                new UsernamePasswordAuthenticationToken(authenticationDTO.username(),
                        authenticationDTO.password());

        String username = null;
        String fullName = null;
        String role = null;
        Integer userId = null;

        try {
            Authentication auth = authenticationManager.authenticate(authInputToken);
            if (auth != null && auth.getPrincipal() instanceof ReaderDetails readerDetails) {
                username = readerDetails.getUsername();
                fullName = readerDetails.reader().getFullName();
                // Extract the role without the "ROLE_" prefix
                role = readerDetails.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .map(authRole -> authRole.replace("ROLE_", ""))
                        .findFirst()
                        .orElse(null);
                userId = readerDetails.reader().getId();
            } else if (auth != null && auth.getPrincipal() instanceof LibrarianDetails librarianDetails) {
                username = librarianDetails.getUsername();
                fullName = librarianDetails.librarian().getFullName();
                // Extract the role without the "ROLE_" prefix
                role = librarianDetails.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .map(authRole -> authRole.replace("ROLE_", ""))
                        .findFirst()
                        .orElse(null);
                userId = librarianDetails.librarian().getId();
            } else if (auth != null && auth.getPrincipal() instanceof AdminDetails adminDetails) {
                username = adminDetails.getUsername();
                fullName = adminDetails.admin().getFullName();
                // Extract the role without the "ROLE_" prefix
                role = adminDetails.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .map(authRole -> authRole.replace("ROLE_", ""))
                        .findFirst()
                        .orElse(null);
                userId = adminDetails.admin().getId();
            }

        } catch (BadCredentialsException e) {
            return Map.of("message", "Incorrect credentials");
        }

        log.info("role: {}", role);
        String token = jwtUtil.generateToken(username, fullName, role, userId);
        log.info("Authentication token: {}", token);
        return Map.of("jwt-token", token);
    }

}
