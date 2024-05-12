package ua.karazin.interfaces.ProjectLibrary.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.karazin.interfaces.ProjectLibrary.dto.LibrariansInfoDTO;
import ua.karazin.interfaces.ProjectLibrary.exceptions.NotAuthenticatedException;
import ua.karazin.interfaces.ProjectLibrary.models.Librarian;
import ua.karazin.interfaces.ProjectLibrary.security.LibrarianDetails;
import ua.karazin.interfaces.ProjectLibrary.security.ReaderDetails;
import ua.karazin.interfaces.ProjectLibrary.services.LibrarianService;

import java.util.HashMap;
import java.util.Map;

@Slf4j(topic = "LibrarianController")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/librarian")
@RestController
public class LibrarianController {
    private final LibrarianService librarianService;

    @GetMapping("/info")
    public LibrariansInfoDTO getLibrariansInfo(Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof LibrarianDetails librarianDetails) {
            Librarian librarian = librarianDetails.librarian();

            return new LibrariansInfoDTO(
                    librarian.getId(),
                    librarian.getFullName(),
                    librarian.getPhoneNumber(),
                    librarian.getEmail(),
                    librarian.getProfilePhoto()
            );
        } else
            throw new NotAuthenticatedException();
    }

    @GetMapping("photo")
    public Map<String, String> getPhoto(Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof LibrarianDetails librarianDetails) {
            int librarianId = librarianDetails.librarian().getId();
            Map<String, String> res = new HashMap<>();
            String photo = librarianService.getLibrariansPhoto(librarianId);
            res.put("photo", photo);
            log.info("res: {}", res);
            return res;
        } else throw new NotAuthenticatedException();
    }
}
