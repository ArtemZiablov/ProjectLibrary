package ua.karazin.interfaces.ProjectLibrary.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
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
            return readerDetailsService.loadUserByUsername(username);
        } catch (ReaderNotExistException e) {
            return librarianDetailsService.loadUserByUsername(username);
        } catch (LibrarianNotExistException e) {
            return adminDetailsService.loadUserByUsername(username);
        }
    }
}
