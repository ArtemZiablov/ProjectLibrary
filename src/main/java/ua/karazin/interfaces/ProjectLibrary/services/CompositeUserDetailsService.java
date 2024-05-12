package ua.karazin.interfaces.ProjectLibrary.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ua.karazin.interfaces.ProjectLibrary.exceptions.AdminNotExistException;
import ua.karazin.interfaces.ProjectLibrary.exceptions.LibrarianNotExistException;
import ua.karazin.interfaces.ProjectLibrary.exceptions.ReaderNotExistException;

@Slf4j(topic = "CompositeUserDetailsService")
@Service
@RequiredArgsConstructor
public class CompositeUserDetailsService implements UserDetailsService {

    private final ReaderDetailsService readerDetailsService;

    private final LibrarianDetailsService librarianDetailsService;

    private final AdminDetailsService adminDetailsService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            log.info("loadUserByUsername, username: {}", username);
            var user =  readerDetailsService.loadUserByUsername(username);
            log.info("loadUserByUsername: {}", user.getUsername());
            return user;
        } catch (ReaderNotExistException e) {
            log.debug("Reader not found, trying librarian", e);
        }

        try {
            return librarianDetailsService.loadUserByUsername(username);
        } catch (LibrarianNotExistException e) {
            log.debug("Librarian not found, trying admin", e);
        }

        try {
            return adminDetailsService.loadUserByUsername(username);
        } catch (AdminNotExistException e) {
            log.error("User not found in any service", e);
            throw new UsernameNotFoundException("User not found in any service for username: " + username, e);
        }
    }
}
