package ua.karazin.interfaces.ProjectLibrary.controllers;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ua.karazin.interfaces.ProjectLibrary.dto.AuthenticationDTO;
import ua.karazin.interfaces.ProjectLibrary.dto.RegisterReaderDTO;
import ua.karazin.interfaces.ProjectLibrary.security.JWTUtil;
import ua.karazin.interfaces.ProjectLibrary.security.ReaderDetails;
import ua.karazin.interfaces.ProjectLibrary.services.RegistrationService;
import org.springframework.security.core.GrantedAuthority;

import java.util.Map;


@RestController
@Slf4j(topic = "AuthController")
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final RegistrationService registrationService;
    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;


    /*@PostMapping("/registration/reader")
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
    }*/

    @GetMapping("/registration")
    public String registrationPage(@ModelAttribute("person") RegisterReaderDTO person){

        return "auth/registration";
    }

    /*@PostMapping("/registration")
    public Map<String, String> performRegistration(@RequestBody @Valid RegisterReaderDTO reader,
                                                   BindingResult bindingResult){

        log.info("request t");
        var readerToRegister = new Reader(
                reader.fullName(),
                reader.dateOfBirth(),
                reader.password(),
                reader.phoneNumber(),
                reader.email(),
                reader.photo()
        );


        if (bindingResult.hasErrors())
            return Map.of("message", "error");

        registrationService.registerReader(readerToRegister);

        String token = jwtUtil.generateToken(readerToRegister.getFullName());

        return Map.of("jwt-token", token);
    }*/

   /* @GetMapping("/login")
    public String loginPage(){
        return "auth/login";
    }*/


    @PostMapping("login")
    public Map<String, String> performLogin(@RequestBody AuthenticationDTO authenticationDTO){
        log.info("Performing login: " + authenticationDTO.username());

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
                log.info("role: {}", role);
                userId = readerDetails.reader().getId();

            }
        } catch (BadCredentialsException e){
            return Map.of("message", "Incorrect credentials");
        }

        String token = jwtUtil.generateToken(username, fullName, role, userId);
        log.info("Authentication token: {}", token);
        return Map.of("jwt-token", token);
    }
}
