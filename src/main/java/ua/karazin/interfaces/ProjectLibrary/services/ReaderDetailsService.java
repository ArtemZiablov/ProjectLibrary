package ua.karazin.interfaces.ProjectLibrary.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ua.karazin.interfaces.ProjectLibrary.exceptions.ReaderNotExistException;
import ua.karazin.interfaces.ProjectLibrary.repositories.ReaderRepo;
import ua.karazin.interfaces.ProjectLibrary.security.ReaderDetails;

@Service
@RequiredArgsConstructor
public class ReaderDetailsService implements UserDetailsService {
    private final ReaderRepo readerRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return readerRepo.findByFullName(username)
                .map(ReaderDetails::new)
                .orElseThrow(ReaderNotExistException::new);
    }
}
