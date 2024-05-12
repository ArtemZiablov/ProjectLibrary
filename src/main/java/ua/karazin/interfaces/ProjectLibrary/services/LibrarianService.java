package ua.karazin.interfaces.ProjectLibrary.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ua.karazin.interfaces.ProjectLibrary.models.Librarian;
import ua.karazin.interfaces.ProjectLibrary.repositories.LibrarianRepo;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LibrarianService {
    private final LibrarianRepo librarianRepo;

    public void save(Librarian librarian) {
        librarianRepo.save(librarian);
    }

    public Optional<Librarian> findLibrarianById(int librarianId){
        return librarianRepo.findLibrarianById(librarianId);
    }

    public Librarian findByFullName(String username){
        return librarianRepo.findByFullName(username)
                .orElseThrow(() -> new UsernameNotFoundException("Librarian with username " + username + " not found"));
    }

    public Librarian findByEmail(String username){
        return librarianRepo.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Librarian with username " + username + " not found"));
    }

    public String getLibrariansPhoto(Integer librarianId) {
        return librarianRepo.findLibrariansPhoto(librarianId);
    }
}
