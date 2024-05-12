package ua.karazin.interfaces.ProjectLibrary.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ua.karazin.interfaces.ProjectLibrary.exceptions.LibrarianNotExistException;
import ua.karazin.interfaces.ProjectLibrary.repositories.LibrarianRepo;
import ua.karazin.interfaces.ProjectLibrary.security.LibrarianDetails;

@Service
@RequiredArgsConstructor
public class LibrarianDetailsService implements UserDetailsService {
    private final LibrarianRepo librarianRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return librarianRepo.findByEmail(username)
                .map(LibrarianDetails::new)
                .orElseThrow(LibrarianNotExistException::new);
    }
}
